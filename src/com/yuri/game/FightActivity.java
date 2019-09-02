package com.yuri.game;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuri.game.controller.listeners.DuelRequestListener;
import com.yuri.game.controller.listeners.FightListener;
import com.yuri.game.model.actor.Actor;
import com.yuri.game.model.actor.FightEnemy;
import com.yuri.game.model.actor.GenderType;
import com.yuri.game.model.actor.Player;
import com.yuri.game.model.duel.AttackState;
import com.yuri.game.model.duel.AttackTarget;
import com.yuri.game.model.duel.BlockTarget;
import com.yuri.game.model.duel.DuelMessage;
import com.yuri.game.model.duel.FightState;
import com.yuri.game.model.listeners.ActorStatsListener;
import com.yuri.game.network.ServerRequestFormer;
import com.yuri.game.ui.activities.MainActivity;
import com.yuri.game.ui.adapters.DuelMessageAdapter;
import com.yuri.game.ui.fragments.AttackBlockFragment;
import com.yuri.game.ui.fragments.AttackBlockFragment.OnCheckBoxSelectedListener;
import com.yuri.game.ui.fragments.DuelBluePlayerFragment;
import com.yuri.game.ui.fragments.DuelFinishHimFragment;
import com.yuri.game.ui.fragments.DuelFinishHimFragment.OnFinishHimListener;
import com.yuri.game.ui.fragments.DuelLossFragment;
import com.yuri.game.ui.fragments.DuelReturnFragment;
import com.yuri.game.ui.fragments.DuelReturnFragment.OnDuelReturnListener;
import com.yuri.game.ui.fragments.DuelVictoryFragment;
import com.yuri.game.ui.fragments.DuelWaitingFragment;

public class FightActivity extends FragmentActivity implements
		OnCheckBoxSelectedListener, FightListener, OnFinishHimListener,
		OnDuelReturnListener, DuelRequestListener, ActorStatsListener {
	
	private static final int HUMAN_MALE_ONE = R.drawable.human_male_one;
	private static final int HUMAN_MALE_TWO = R.drawable.human_male_two;
	private static final int HUMAN_FEMALE_ONE = R.drawable.human_female_one;
	private static final int HUMAN_FEMALE_TWO = R.drawable.human_female_two;
	private static final int ORC_MALE = R.drawable.orc_male;
	private static final int ORC_FEMALE = R.drawable.orc_female;
	private static final int ELF_MALE = R.drawable.elf_male;
	private static final int ELF_FEMALE = R.drawable.elf_female;
	private static final int UNDEAD_MALE = R.drawable.undead_male;
	private static final int UNDEAD_FEMALE = R.drawable.undead_female;

	private HpBar redHp;
	private DuelMessageAdapter duelMessageAdapter;
	private FragmentManager fragmentManager;
	private AttackBlockFragment attackBlockFragment;
	private GameApplication app;

	private List<DuelMessage> duelMessages;
	private FightEnemy fightEnemy;
	private DuelMessage fightStartMsg;
	
	private WeakReference<Actor> actor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_fight);
		
		app = GameApplication.getApplicationFromActivityContext(this);
		
		redHp = (HpBar) findViewById(R.id.player1HpBar);
		ImageView redPlayerImage = (ImageView) findViewById(R.id.iv_red_player);
		
		app.controllers.actorStatsController.actorStatsListeners.add(this);
		

		fightStartMsg = getIntent().getExtras().getParcelable("START_MESSAGE");
		fightEnemy = getIntent().getExtras().getParcelable("FIGHT_ENEMY");
		
		setRedPlayerImage(redPlayerImage);

		fragmentManager = getSupportFragmentManager();
		

		attackBlockFragment = new AttackBlockFragment();

		fragmentManager.beginTransaction()
				.replace(R.id.fragment_top_container, attackBlockFragment)
				.commit();
		DuelBluePlayerFragment bluePlayerFragment = new DuelBluePlayerFragment();

		Bundle b = new Bundle();
		b.putParcelable("FIGHT_ENEMY", fightEnemy);
		bluePlayerFragment.setArguments(b);

		fragmentManager.beginTransaction()
				.replace(R.id.fragment_right_container, bluePlayerFragment)
				.commit();

		if (savedInstanceState == null) {

			Log.e("savedInstanceState == null", "fightStartMsg = "
					+ fightStartMsg);
			Log.e("savedInstanceState == null", "fightEnemy = " + fightEnemy);

			processFightStart(fightStartMsg);

			duelMessages = new ArrayList<DuelMessage>();
			duelMessageAdapter = new DuelMessageAdapter(this, 0, duelMessages);
			ListView listDuelMessage = (ListView) findViewById(R.id.list_duel_message);
			listDuelMessage.setAdapter(duelMessageAdapter);

		} else {
			ListView listDuelMessage = (ListView) findViewById(R.id.list_duel_message);
			duelMessages = savedInstanceState.getParcelableArrayList("ADAPTER");
			duelMessageAdapter = new DuelMessageAdapter(this, 0, duelMessages);
			listDuelMessage.setAdapter(duelMessageAdapter);
			Fragment fragment = fragmentManager
					.findFragmentById(R.id.fragment_top_container);
			if (fragment instanceof AttackBlockFragment) {
				attackBlockFragment = (AttackBlockFragment) fragment;
			}
		}
		
		setBluePlayerImage(fightEnemy);
	}

	private void setBluePlayerImage(FightEnemy fightEnemy2) {
		app.controllers.networkController.sendRequest(new ServerRequestFormer()
				.getAnyChar(fightEnemy2.getName()));

	}

	private void setRedPlayerImage(ImageView redPlayerImage) {
		Player player = app.world.modelContainer.player;
		setPlayerIcon(redPlayerImage, player);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		notifyAdapters();

		app.controllers.fightController.fightListeners.add(this);

		redHp.setMax(app.world.modelContainer.player.getMaxHP());
		redHp.setProgress(app.world.modelContainer.player.getCurrentHP());
		redHp.setText("" + app.world.modelContainer.player.getCurrentHP() + "/"
				+ app.world.modelContainer.player.getMaxHP());

	}

	private void notifyAdapters() {
		WeakHashMap<Class<?>, Object> locAdapters = app.world.modelContainer.location.locAdapters;

		for (WeakHashMap.Entry<Class<?>, Object> entry : locAdapters.entrySet()) {
			Object value = entry.getValue();

			if (value instanceof ArrayAdapter<?> && value != null) {
				ArrayAdapter<?> ar = (ArrayAdapter<?>) value;
				ar.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		app.controllers.fightController.fightListeners.remove(this);
		app.controllers.actorStatsController.actorStatsListeners.remove(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelableArrayList("ADAPTER",
				(ArrayList<? extends Parcelable>) duelMessages);

	}

	@Override
	public void playerActions(HashMap<String, Integer> result) {

		fragmentManager
				.beginTransaction()
				.replace(R.id.fragment_right_container,
						new DuelWaitingFragment()).commit();

		AttackTarget attack = AttackTarget.values()[result.get("ATTACK")];
		BlockTarget block1 = BlockTarget.values()[result.get("BLOCK1")];
		BlockTarget block2 = BlockTarget.values()[result.get("BLOCK2")];

		app.controllers.networkController.sendRequest(new ServerRequestFormer()
				.fightAttack(attack, block1, block2));

	}

	@Override
	public void onFightMessage(DuelMessage message) {
		if (message.getFightState() != null) {
			String fightStage = message.getFightState().getType();
			if (fightStage.equals("fight_start")) {
				processFightStart(message);
			}
			if (fightStage.equals("fight_end")) {
				processFightEnd(message);
			}
		} else {
			processFightGoing(message);
		}
	}

	private void processFightGoing(DuelMessage message) {

		String playerName = app.world.modelContainer.player.getName();
		String receiver;

		if (message.getFirstAttack() != null) {
			if (message.getFirstAttack().getDamage() != 0) {
				receiver = message.getFirstAttack().getReceiver();
				if (playerName.equals(receiver)) {
					redHp.setProgress(message.getFirstAttack().getCurrentHp());
					redHp.setText("[" + message.getFirstAttack().getCurrentHp()
							+ "/" + message.getFirstAttack().getMaxHp() + "]");
				} else {

					DuelBluePlayerFragment bluePlayerFragment = new DuelBluePlayerFragment();
					Bundle bundle = new Bundle();
					bundle.putParcelable("DUEL_MESSAGE", message);
					bundle.putInt("ATTACK_NUMBER", 1);
					bluePlayerFragment.setArguments(bundle);

					fragmentManager
							.beginTransaction()
							.replace(R.id.fragment_right_container,
									bluePlayerFragment).commit();

				}
			} else {

				DuelBluePlayerFragment bluePlayerFragment = new DuelBluePlayerFragment();
				Bundle bundle = new Bundle();
				bundle.putParcelable("DUEL_MESSAGE", message);
				bundle.putInt("ATTACK_NUMBER", 1);
				bluePlayerFragment.setArguments(bundle);

				fragmentManager
						.beginTransaction()
						.replace(R.id.fragment_right_container,
								bluePlayerFragment).commit();
			}
		}

		if (message.getSecondAttack() != null) {
			if (message.getFirstAttack().getDamage() != 0) {
				receiver = message.getSecondAttack().getReceiver();
				if (playerName.equals(receiver)) {
					redHp.setProgress(message.getSecondAttack().getCurrentHp());
					redHp.setText("["
							+ message.getSecondAttack().getCurrentHp() + "/"
							+ message.getSecondAttack().getMaxHp() + "]");
				} else {

					DuelBluePlayerFragment bluePlayerFragment = new DuelBluePlayerFragment();
					Bundle bundle = new Bundle();
					bundle.putParcelable("DUEL_MESSAGE", message);
					bundle.putInt("ATTACK_NUMBER", 2);
					bluePlayerFragment.setArguments(bundle);

					fragmentManager
							.beginTransaction()
							.replace(R.id.fragment_right_container,
									bluePlayerFragment).commit();

				}
			} else {
				DuelBluePlayerFragment bluePlayerFragment = new DuelBluePlayerFragment();
				Bundle bundle = new Bundle();
				bundle.putParcelable("DUEL_MESSAGE", message);
				bundle.putInt("ATTACK_NUMBER", 2);
				bluePlayerFragment.setArguments(bundle);

				fragmentManager
						.beginTransaction()
						.replace(R.id.fragment_right_container,
								bluePlayerFragment).commit();
			}
		}

		duelMessageAdapter.add(message);
	}

	/*
	 * Рассмотреть вариант ничьи позже
	 */
	private void processFightEnd(DuelMessage message) {
		FightState fightState = message.getFightState();
		String winner = fightState.getWinner();

		if (winner.equals("red")) {
			winner = fightState.getRed();
		} else if (winner.equals("blue")) {
			winner = fightState.getBlue();
		}

		if (app.world.modelContainer.player.getName().equals(winner)) {
			fragmentManager
					.beginTransaction()
					.replace(R.id.fragment_right_container,
							new DuelVictoryFragment()).commit();
		} else {
			fragmentManager
					.beginTransaction()
					.replace(R.id.fragment_right_container,
							new DuelLossFragment()).commit();
		}

		fragmentManager.beginTransaction()
				.replace(R.id.fragment_top_container, new DuelReturnFragment())
				.commit();
	}

	private void processFightStart(DuelMessage message) {
		TextView startText = (TextView) findViewById(R.id.tv_duel_start_message);
		startText.setText("" + message.getFightState().getRed() + " vs "
				+ message.getFightState().getBlue());
	}

	@Override
	public void onFightUnregistered() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnemyAttackState(AttackState attackState, String target) {
		switch (attackState) {
		case ATTACKABLE:
			makePlayerAttackable();
			break;
		case NOT_ATTACKABLE:
			makePlayerNotAttackable();
			break;
		case FINISH_HIM:
			doFinishHim();
			break;
		case NOT_FINISHED:
			// To be implemented later
			break;
		}
	}

	private void doFinishHim() {
		fragmentManager
				.beginTransaction()
				.replace(R.id.fragment_top_container,
						new DuelFinishHimFragment()).commit();
	}

	private void makePlayerNotAttackable() {
		findViewById(R.id.fragment_top_container).setVisibility(View.GONE);
	}

	private void makePlayerAttackable() {
		findViewById(R.id.fragment_top_container).setVisibility(View.VISIBLE);
	}

	@Override
	public void onEnemyInitStats(FightEnemy enemy) {

	}

	@Override
	public void onFightRegistered() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinishHim() {
		app.controllers.networkController.sendRequest(new ServerRequestFormer()
				.finishEnemyPlayer());
	}

	@Override
	public void onDuelReturn() {
		Intent i = new Intent(FightActivity.this, MainActivity.class);
		startActivity(i);
	}

	@Override
	public void onDuelRequestListReceived() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDuelRequestPublished() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDuelRequestRemoved() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDuelIsFull(String owner, String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewPlayerAddedToDuel(String owner, String challenger) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDuelToStartNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDuelStartFail() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDuelToRemoveNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDuelToAddNewPlayerNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDuelToRemovePlayerFromNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerRemovedFromDuel(String owner, String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerStatsChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActorStatsRequested() {
		actor = new WeakReference<Actor>(app.world.modelContainer.actor);
		Actor a = actor.get();
		Player p = null;
		
		if (a != null) {
			try {
				p = (Player) a;
			} catch (ClassCastException e) {
				Log.e("onActorStatsRequested", "Player-Actor Cast Exception: " + e.toString());
			}
		}
		
		if (p != null) {
			Fragment f = fragmentManager.findFragmentById(R.id.fragment_right_container);
			if (f != null && f instanceof DuelBluePlayerFragment) {
				DuelBluePlayerFragment dbpf = (DuelBluePlayerFragment) f;
				ImageView bluePlayerImage = (ImageView) dbpf.getView().findViewById(R.id.iv_blue_player);
				setPlayerIcon(bluePlayerImage, p);
			}
		}
	}
	
	private void setPlayerIcon(ImageView ivPlayerIcon, Player player) {
		switch (player.getRace()) {
		case HUMAN:
			if (player.getGender() == GenderType.MALE) {
				ivPlayerIcon.setImageResource(HUMAN_MALE_ONE);
			} else {
				ivPlayerIcon.setImageResource(HUMAN_FEMALE_ONE);
			}
			break;
		case ORC:
			if (player.getGender() == GenderType.MALE) {
				ivPlayerIcon.setImageResource(ORC_MALE);
			} else {
				ivPlayerIcon.setImageResource(ORC_FEMALE);
			}
			break;
		case ELF:
			if (player.getGender() == GenderType.MALE) {
				ivPlayerIcon.setImageResource(ELF_MALE);
			} else {
				ivPlayerIcon.setImageResource(ELF_FEMALE);
			}
			break;
		case UNDEAD:
			if (player.getGender() == GenderType.MALE) {
				ivPlayerIcon.setImageResource(UNDEAD_MALE);
			} else {
				ivPlayerIcon.setImageResource(UNDEAD_FEMALE);
			}
			break;
		}
	}

	@Override
	public void onCharNotFound(String message) {
		// TODO Auto-generated method stub
		
	}
}
