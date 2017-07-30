package edu.cs4730.btDemo;

import java.util.UUID;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/*
 * most of the work is done in the fragments.  You will need to install this example onto 2 devices
 * with bluetooth in order to make this example work.
 *
 * The help fragment starts up first, to check and see if this example will work.
 * Server fragment is the for the "server" code version with bluetooth.
 * Client fragment is will connect to the server code.
 */

public class MainActivity extends AppCompatActivity implements Help_Fragment.OnFragmentInteractionListener {


	public static final UUID MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
	public static final String NAME = "BluetoothDemo";

	FragmentManager fragmentManager;
	Fragment fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.frag_container, new Help_Fragment()).commit();

	}

	@Override
	public void onButtonSelected(int id) {
		
		FragmentTransaction transaction =fragmentManager.beginTransaction();
		// Replace whatever is in the fragment_container view with this fragment,
		if (id == 2) { //client
			fragment = new Client_Fragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragment).commit();
			//transaction.replace(R.id.frag_container, new Client_Fragment());
		} else { //server
			transaction.replace(R.id.frag_container, new Server_Fragment());
		}
		// and add the transaction to the back stack so the user can navigate back
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}

}
