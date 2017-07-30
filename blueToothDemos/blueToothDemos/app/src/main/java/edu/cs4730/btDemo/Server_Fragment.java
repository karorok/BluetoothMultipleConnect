package edu.cs4730.btDemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Server_Fragment extends Fragment {

    BluetoothAdapter mBluetoothAdapter =null;


    TextView output;
    Button btn_start;
    Button btn_start1;
    Button btn_finish;
    Button btn_send;

    int count=0;

    int connectI = 1;

    private ArrayList<BluetoothSocket> mSockets;

    AcceptThread myThread;
    AcceptThread myThread1;

    BluetoothServerSocket mmServerSocket;

    public Server_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_server, container, false);
        //text field for output info.
        output = (TextView) myView.findViewById(R.id.sv_output);
        mSockets = new ArrayList<BluetoothSocket>();

        btn_start = (Button) myView.findViewById(R.id.start_server);
        btn_start1 = (Button) myView.findViewById(R.id.start_server1);
        btn_finish = (Button) myView.findViewById(R.id.finish_server);
        btn_send = (Button)myView.findViewById(R.id.btn_send);

        btn_start.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                output.append("Starting server\n");
                //startServer();
                myThread = new AcceptThread();
                new Thread(myThread).start();

            }
        });

        btn_start1.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                output.append("Starting server\n");
                //startServer();
                myThread1 = new AcceptThread();
                new Thread(myThread1).start();
            }
        });


        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myThread.cancel();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                    myThread.send();
                    myThread1.send();

            }
        });


        //setup the bluetooth adapter.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            output.append("No bluetooth device.\n");
            btn_start.setEnabled(false);
        }

        return myView;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            output.append(msg.getData().getString("msg"));
        }

    };
    public void mkmsg(String str) {
        //handler junk, because thread can't update screen!
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", str);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    public void startServer() {
        myThread = new AcceptThread();
        new Thread(myThread).start();
        myThread1 = new AcceptThread();
        new Thread(myThread1).start();

    }


    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket

        private BluetoothSocket socket = null;

        public AcceptThread() {
            /*BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(MainActivity.NAME, MainActivity.MY_UUID);
            } catch (IOException e) {
                mkmsg("Failed to start server\n");
            }
            mmServerSocket = tmp;
            */
        }

        public void send(){
            try {
                if(socket != null) {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    out.println(count);
                    out.flush();
                }
            }catch(IOException e){}
        }


        public void run() {
            mkmsg("waiting on accept");
            //BluetoothSocket socket = null;
            /*
            try {
                // Listen for all 7 UUIDs
                for (int i = 0; i < 2; i++) {

                    socket = mmServerSocket.accept();
                    if (socket != null) {
                        mSockets.add(socket);
                        mkmsg("bluetooth connected number : "+i);
                    }
                }
            } catch (IOException e) {
            }
*/

            try {
                mmServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(MainActivity.NAME, MainActivity.MY_UUID);
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
                if (socket != null) {
                    mSockets.add(socket);
                    mkmsg("bluetooth connected number : "+connectI++);
                }
            } catch (IOException e) {
                mkmsg("Failed to accept\n");
            }




            mkmsg("Connection made\n");
            // If a connection was accepted
            if (socket != null) {

                try {
                    mmServerSocket.close();
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    mkmsg("Attempting to send message ...\n");
                    out.println("Reponse from Bluetooth Demo Server");
                    out.flush();
                    mkmsg("Message sent...\n");
                }catch(IOException e){}
/*
                mkmsg("Remote device address: "+socket.getRemoteDevice().getAddress().toString()+"\n");
                //Note this is copied from the TCPdemo code.
                try {
                    mkmsg("Attempting to receive a message ...\n");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String str = in.readLine();
                    mkmsg("received a message:\n" + str+"\n");

                    PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
                    mkmsg("Attempting to send message ...\n");
                    out.println("Reponse from Bluetooth Demo Server");
                    out.flush();
                    mkmsg("Message sent...\n");

                    mkmsg("We are done, closing connection\n");
                } catch(Exception e) {
                    mkmsg("Error happened sending/receiving\n");

                }

                */

                while(socket != null){

                }
            } else {
                mkmsg("Made connection, but socket is null\n");
            }

        }

        public void cancel() {
            try {
                mmServerSocket.close();
                mkmsg("Server ending \n");
                mkmsg("cancel thread closed");
            } catch (IOException e) {

            }
        }
    }

}