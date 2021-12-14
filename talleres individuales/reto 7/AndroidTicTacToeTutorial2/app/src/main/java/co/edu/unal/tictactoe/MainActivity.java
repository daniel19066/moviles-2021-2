package co.edu.unal.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


public class MainActivity extends AppCompatActivity {
    // Buttons making up the board
    private Button boardButtons[];
    char mBoard1[] ;
    char juegocon;
    private boolean singleplayer;
    private Map<String, Object> juegoOnline ;
    private String turnoOnline;
    private Toast toast;
    private boolean isMyMove;
    private int selected;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean endgame=false;
    // Various text displayed
    private TextView mInfoTextView,pcScore,human,empate;
    private TicTacToeGame mGame;
    private boolean GameOver,humanStarts;
    private Integer humanSc=0,androidScore=0,empateScore=0;
    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    private BoardView mBoardView;
    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;
    private SharedPreferences mPrefs;
    private String roomName;

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.robot);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(!singleplayer) {
            juegoOnline.put("0", " ");
            juegoOnline.put("1", " ");
            juegoOnline.put("2", " ");
            juegoOnline.put("3", " ");
            juegoOnline.put("4", " ");
            juegoOnline.put("5", " ");
            juegoOnline.put("6", " ");
            juegoOnline.put("7", " ");
            juegoOnline.put("8", " ");
            juegoOnline.put("turno1", "");
            juegoOnline.put("turno2", "");
            juegoOnline.put("turnoactual", "1");
            db.collection("board").document(roomName)
                    .set(juegoOnline)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("funciono escribir");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("no funciono escribir");
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                if(!singleplayer) {
                    juegoOnline.put("0", " ");
                    juegoOnline.put("1", " ");
                    juegoOnline.put("2", " ");
                    juegoOnline.put("3", " ");
                    juegoOnline.put("4", " ");
                    juegoOnline.put("5", " ");
                    juegoOnline.put("6", " ");
                    juegoOnline.put("7", " ");
                    juegoOnline.put("8", " ");
                    juegoOnline.put("turno1", "");
                    juegoOnline.put("turno2", "");
                    db.collection("board").document(roomName)
                            .set(juegoOnline)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    System.out.println("funciono escribir");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("no funciono escribir");
                                }
                            });
                }
                startNewGame();
                endgame=false;
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};
// TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
// selected is the radio button that should be selected.
                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss(); // Close dialog
// TODO: Set the diff level of mGame based on which item was selected.
// Display the selected difficulty level
                                switch (item){
                                    case 0:

                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                        break;

                                    case 1:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                        break;

                                    case 2:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                        break;
                                }

                                Toast.makeText(getApplicationContext(), levels[item], Toast.LENGTH_SHORT).show();

                            }
                        });
                dialog = builder.create();
                break;

            case DIALOG_QUIT_ID:
// Create the quit confirmation dialog
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.resetScore();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGame= new TicTacToeGame();
        singleplayer=getIntent().getBooleanExtra("singleUser",true);
        setContentView(R.layout.activity_main);
        boardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoard1 = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        // Listen for touches on the board
        mBoardView.setOnTouchListener(mTouchListener);
        isMyMove=false;
        mInfoTextView = (TextView) findViewById(R.id.information);
        human = (TextView) findViewById(R.id.humanScore);
        pcScore = (TextView) findViewById(R.id.pcScore);
        empate = (TextView) findViewById(R.id.empate);
        human.setText(humanSc.toString());
        pcScore.setText(androidScore.toString());
        empate.setText(empateScore.toString());
        humanStarts=true;
        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        selected = 0;
        mPrefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
        humanSc = mPrefs.getInt("mHumanWins", 0);
        androidScore = mPrefs.getInt("mComputerWins", 0);
        empateScore = mPrefs.getInt("mTies", 0);
        roomName= getIntent().getExtras().getString("name");
        if (savedInstanceState == null) {
            startNewGame();
        }
        else {
// Restore the game's state
            mGame.setmBoard(savedInstanceState.getCharArray("board"));
            GameOver = savedInstanceState.getBoolean("mGameOver");
            mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
            humanSc = savedInstanceState.getInt("mHumanWins");
            androidScore = savedInstanceState.getInt("mComputerWins");
            empateScore = savedInstanceState.getInt("mTies");
            humanStarts = savedInstanceState.getChar("mGoFirst")=='h'?true:false;

        }
        displayScores();
        if(!singleplayer) {
            db.collection("board").document(roomName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    System.out.println(document.getData());
                                    juegoOnline=document.getData();

                                    if(juegoOnline.get("turno1")==""){
                                        juegoOnline.put("turno1","ocupado");
                                        System.out.println(juegoOnline);
                                        turnoOnline="1";
                                        db.collection("board").document(roomName)
                                                .set(juegoOnline)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        System.out.println("funciono escribir");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        System.out.println("no funciono escribir");
                                                    }
                                                });
                                    }else if(juegoOnline.get("turno2")==""){
                                        juegoOnline.put("turno2","ocupado");
                                        System.out.println(juegoOnline);
                                        turnoOnline="2";

                                        db.collection("board").document(roomName)
                                                .set(juegoOnline)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        System.out.println("funciono escribir");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        System.out.println("no funciono escribir");
                                                    }
                                                });
                                    }else{
                                        System.out.println("sala ocupada");
                                    }
                                    if (turnoOnline.equals("1")){
                                        juegocon=TicTacToeGame.HUMAN_PLAYER;
                                    }else{
                                        juegocon=TicTacToeGame.COMPUTER_PLAYER;
                                    }
                                    if(juegoOnline.get("turno2").equals("")){
                                        mBoardView.setEnabled(false);
                                        mInfoTextView.setText("esperando segundo jugador");
                                    } else if (juegoOnline.get("turno2").equals("ocupado") && juegoOnline.get("turno1").equals("ocupado")) {
                                        if(juegoOnline.get("turnoactual").equals(turnoOnline)){
                                            mInfoTextView.setText("tu turno");
                                        }else{
                                            mInfoTextView.setText("It's other player turn.");
                                        }
                                    }
                                } else {
                                    System.out.println("alv ese documento esta joto");
                                }
                            } else {
                                System.out.println("alv no sirvio");
                            }
                        }
                    });
            db.collection("board")
                    .whereEqualTo("turno1", "ocupado").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {

                        return;
                    }

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:

                                break;
                            case MODIFIED:
                                juegoOnline =  dc.getDocument().getData();
                                if(isMyMove==true){
                                    System.out.println("vamos :2");
                                }
                                else{
                                    for(int i=0;i<mBoard1.length;i++){
                                        String aux= (String) juegoOnline.get(String.valueOf(i));
                                        mBoard1[i]=aux.charAt(0);
                                    }
                                    mGame.setmBoard(mBoard1);
                                    mBoardView.invalidate();
                                    mBoardView.setEnabled(true);
                                    if(juegoOnline.get("turno2").equals("")){
                                        mBoardView.setEnabled(false);
                                        mInfoTextView.setText("esperando segundo jugador");
                                    }else{


                                        if(juegoOnline.get("turnoactual").equals(turnoOnline)){
                                            mInfoTextView.setText("tu turno");
                                            mBoardView.setEnabled(true);
                                        }else{
                                            mInfoTextView.setText("It's other player turn.");
                                            mBoardView.setEnabled(false);
                                        }
                                        final int[] winner = {mGame.checkForWinner()};
                                        if (winner[0] == 0) {

                                        }
                                        else if (winner[0] == 1)
                                        {
                                            mInfoTextView.setText("It's a tie!");
                                            GameOver=true;
                                            empateScore++;
                                            empate.setText(empateScore.toString());
                                            endgame=true;
                                            mBoardView.setEnabled(false);
                                        }

                                        else if (winner[0] == 2) {


                                            mInfoTextView.setText("You won!");
                                            mBoardView.setEnabled(false);
                                        }
                                        else {
                                            mInfoTextView.setText("the other player won!");
                                            GameOver = true;
                                            androidScore++;
                                            pcScore.setText(androidScore.toString());
                                            endgame=true;
                                            mBoardView.setEnabled(false);
                                        }

                                    }
                                }
                                break;
                            case REMOVED:

                                break;
                        }
                    }

                }
            });
        }
        /*Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("sirvi :)");
                        System.out.println("xd");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("F");
                    }
                });
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println( document.getId() + " => " + document.getData());
                            }
                        } else {
                            System.out.println("Error getting documents.");
                        }
                    }
                });*/

        /*Map<String, Object> board = new HashMap<>();
        char[] board1=mGame.getmBoard();
        for(int i=0;i<board1.length;i++){
            board.put(String.valueOf(i), String.valueOf(board1[i]));
        }
        db.collection("board").document(roomName)
                .set(board)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       System.out.println("DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document");
                    }
                });
        db.collection("board")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println( document.getId() + " => " + document.getData());
                            }
                        } else {
                            System.out.println("Error getting documents.");
                        }
                    }
                });*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharArray("board", mGame.getmBoard());
        outState.putBoolean("mGameOver", GameOver);
        outState.putInt("mHumanWins", Integer.valueOf(humanSc));
        outState.putInt("mComputerWins", Integer.valueOf(androidScore));
        outState.putInt("mTies", Integer.valueOf(empateScore));
        outState.putCharSequence("info", mInfoTextView.getText());
        outState.putChar("mGoFirst", humanStarts?'h':'a');
    }

    private void displayScores() {
        human.setText(Integer.toString(humanSc));
        pcScore.setText(Integer.toString(androidScore));
        empate.setText(Integer.toString(empateScore));
    }
    private void resetScore(){
        this.humanSc=0;
        this.androidScore=0;
        this.empateScore=0;
        empate.setText(empateScore.toString());
        human.setText(humanSc.toString());
        pcScore.setText(androidScore.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
// Save the current scores
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("mHumanWins", humanSc);
        ed.putInt("mComputerWins", androidScore);
        ed.putInt("mTies", empateScore);
        ed.commit();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGame.setmBoard(savedInstanceState.getCharArray("board"));
        GameOver = savedInstanceState.getBoolean("mGameOver");
        mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
        humanSc = savedInstanceState.getInt("mHumanWins");
        androidScore = savedInstanceState.getInt("mComputerWins");
        empateScore = savedInstanceState.getInt("mTies");
        humanStarts = savedInstanceState.getChar("mGoFirst")=='h'?true:false;
    }


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
// Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if(!singleplayer ){



                final DocumentReference docRef = db.collection("board").document(roomName);

                mBoardView.setEnabled(false);

                if(juegoOnline.get("turnoactual").equals(turnoOnline)){
                    isMyMove=true;
                    if (!endgame && setMove(juegocon, pos)){
// If no winner yet, let the computer make a move
                        isMyMove=false;
                        juegoOnline.put(String.valueOf(pos),String.valueOf(juegocon));
                        final int[] winner = {mGame.checkForWinner()};
                        if (turnoOnline.equals("1")){
                            juegoOnline.put("turnoactual","2");
                        }else{
                            juegoOnline.put("turnoactual","1");
                        }


                        if (winner[0] == 0) {
                            db.collection("board").document(roomName)
                                    .set(juegoOnline)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            System.out.println("funciono escribir");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            System.out.println("no funciono escribir");
                                        }
                                    });

                            mInfoTextView.setText("It's other player turn.");


                        }
                        else if (winner[0] == 1)
                        {db.collection("board").document(roomName)
                                .set(juegoOnline)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        System.out.println("funciono escribir");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("no funciono escribir");
                                    }
                                });
                            mInfoTextView.setText("It's a tie!");
                            GameOver=true;
                            empateScore++;
                            empate.setText(empateScore.toString());
                            endgame=true;
                        }

                        else if (winner[0] == 2) {
                            db.collection("board").document(roomName)
                                    .set(juegoOnline)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            System.out.println("funciono escribir");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            System.out.println("no funciono escribir");
                                        }
                                    });

                            mInfoTextView.setText("You won!");
                            GameOver = true;
                            humanSc++;
                            human.setText(humanSc.toString());
                            endgame=true;
                        }
                        else {
                            mInfoTextView.setText("Android won!");
                            GameOver = true;
                            androidScore++;
                            pcScore.setText(androidScore.toString());
                            endgame=true;
                        }

                    }
                }else{

                    isMyMove=true;


                }

            }


            if (!endgame && setMove(TicTacToeGame.HUMAN_PLAYER, pos)&& singleplayer){
// If no winner yet, let the computer make a move
                final int[] winner = {mGame.checkForWinner()};
                if (winner[0] == 0 ) {
                    final Handler handler = new Handler();
                    mBoardView.setEnabled(false);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            mInfoTextView.setText("It's your turn.");
                            int move = mGame.getComputerMove();
                            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                            mComputerMediaPlayer.start();
                            winner[0] = mGame.checkForWinner();
                            mBoardView.invalidate();
                            mBoardView.setEnabled(true);
                            if (winner[0] == 1)
                            {
                                mInfoTextView.setText("It's a tie!");
                                GameOver=true;
                                empateScore++;
                                empate.setText(empateScore.toString());
                                endgame=true;
                            }

                        }
                    }, 2000);


                }
                if (winner[0] == 0) {
                    mInfoTextView.setText("It's Android's turn.");


                }
                else if (winner[0] == 1)
                {
                    mInfoTextView.setText("It's a tie!");
                    GameOver=true;
                    empateScore++;
                    empate.setText(empateScore.toString());
                    endgame=true;
                }

                else if (winner[0] == 2) {
                    mInfoTextView.setText("You won!");
                    GameOver = true;
                    humanSc++;
                    human.setText(humanSc.toString());
                    endgame=true;
                }
                else {
                    mInfoTextView.setText("Android won!");
                    GameOver = true;
                    androidScore++;
                    pcScore.setText(androidScore.toString());
                    endgame=true;
                }
            }
// So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    private void startNewGame() {
        mGame.clearBoard();
        mBoardView.invalidate();
        this.GameOver = false;
        if(humanStarts) {
            mInfoTextView.setText("you go first.");
            humanStarts=!humanStarts;
        }
        else {
            mInfoTextView.setText("android go first.");
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            humanStarts=!humanStarts;
        }
    }
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        public void onClick(View view) {
            if (boardButtons[location].isEnabled()&& !endgame) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);
                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();

                if (winner == 0 ) {

                    mInfoTextView.setText("It's Android's turn.");
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();

                }
                if (winner == 0) {
                    mInfoTextView.setText("It's your turn.");
                }
                else if (winner == 1)
                {
                    mInfoTextView.setText("It's a tie!");
                    GameOver=true;
                    empateScore++;
                    empate.setText(empateScore.toString());
                    endgame=true;
                }

                else if (winner == 2) {
                    mInfoTextView.setText("You won!");
                    GameOver = true;
                    humanSc++;
                    human.setText(humanSc.toString());
                    endgame=true;
                }
                else {
                    mInfoTextView.setText("Android won!");
                    GameOver = true;
                    androidScore++;
                    pcScore.setText(androidScore.toString());
                    endgame=true;
                }
            }
        }
    }

    private boolean setMove(char player, int location) {
        if (mGame.setMove(player, location)) {
            mBoardView.invalidate(); // Redraw the board
            return true;
        }
        return false;
    }
}










