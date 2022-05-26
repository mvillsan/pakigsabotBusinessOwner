package com.capstone.pakigsabotbusinessowner.HelpCenter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.EmailMessage.EmailMessageLayout;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HelpCenterFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HistoryFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HomeFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ReservationsFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ServicesFragment;
import com.capstone.pakigsabotbusinessowner.R;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpCenter extends AppCompatActivity {

    //Initialize Variables
    ImageView imgBackBtn,gmailBtn;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    AlertDialog.Builder builder;
    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

        //References::
        refs();

        //Alert Dialog
        builder = new AlertDialog.Builder(this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableLV);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragments();
            }
        });

        gmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email();
            }
        });

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Topics",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Closed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                //Declaration of variables::
                String setUpEst = "Setup  Establishment", reserveManage = "Reservation Management", promoDealsManage = "Promo and Deals Management", estAccount = "Your Account", safety = "Safety", pakigsabot= "About Pakigsa-Bot";
                String setUpEstData1 = "View", setUpEstData2 = "Add", setUpEstData3 = "Edit", setUpEstData4 = "Delete";
                String reserveManageData1 = "View Reservations", reserveManageData2 = "Accept/Confirm Reservation", reserveManageData3 = "Cancel Reservation", reserveManageData4 = "Reschedule Reservation", reserveManageData5 = "Reservation History", reserveManageData6= "Delete Reservation";
                String promoDealsManageData1 = "View Promo and Deals", promoDealsManageData2= "Add Promo and Deals", promoDealsManageData3= "Edit Promo and Deals", promoDealsManageData4= "Delete Promo and Deals";
                String estAccountData1 = "Creating an Account", estAccountData2 = "Managing Account", estAccountData3 = "Account Subscription", estAccountData4 = "Account Security";
                String safetyData = "Reporting issues";
                String pakigsabotData ="How it Works";

                //Alert Dialog per item::
                if(setUpEst.equals(listDataHeader.get(groupPosition)) && setUpEstData1.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("(1) To View Menu Items/Services/Procedures/Facilities, you just" +
                            "have to tap the services button. You will then see a button to PROCEED." +
                            "(2) Tap that and you will be redirected to the SetUp Establishment Screen"+
                            "There, you will the button to whether Menu Items/Services/Procedures/Facilities." +
                            "(3) Tap the button and you'll be able to see what you have added.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("View");
                    alert1.show();
                }else if(setUpEst.equals(listDataHeader.get(groupPosition)) && setUpEstData2.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("(1) To Add Menu Items/Services/Procedures/Facilities, you just" +
                            "have to tap the services button. You will then see a button to PROCEED." +
                                    "(2) Tap that and you will be redirected to the SetUp Establishment Screen"+
                                    "There, you will the button to whether Menu Items/Services/Procedures/Facilities." +
                                    "(3) Tap the button and you'll be able to see a (+) button on the upper right corner of the screen" +
                                    "(4) Tap the (+) button and you will be redirected to the screen where you can add Menu Items/Services/Procedures/Facilities")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    alert1.setTitle("Add");
                    alert1.show();
                }else if(setUpEst.equals(listDataHeader.get(groupPosition)) && setUpEstData3.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("(1) To Edit Menu Items/Services/Procedures/Facilities, you just" +
                            "have to tap the pencil-like icon beside its name." +
                                    "(2) A pop-up dialog will appear where you can edit the details"+
                                    "(3) Tap the UPDATE button below the details to save the update/s you have made.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Edit");
                    alert1.show();
                }else if(setUpEst.equals(listDataHeader.get(groupPosition)) && setUpEstData4.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("Unsubscribed Customers will be limited only to reserve upto 2 establishments per day; " +
                            "For Subscribed Customers, they can reserve upto 3 or more establishments per day")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Delete");
                    alert1.show();
                }else if(reserveManage.equals(listDataHeader.get(groupPosition)) && reserveManageData1.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("We’d love to welcome your loved ones into Pakigsa-Bot. But to help maintain transparency and trust throughout our community, " +
                            "you can’t book on their behalf unless you're staying together.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("View Reservations");
                    alert1.show();
                }else if(reserveManage.equals(listDataHeader.get(groupPosition)) && reserveManageData2.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("Keep track on a reservation by tapping the reservations feature on the home screen. " +
                            "It contains all the reservations including its statuses being made by the customer. " +
                            "You can also see in there the reservation history of all the completed/cancelled reservations.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Accept/Confirm Reservation");
                    alert1.show();
                }else if(reserveManage.equals(listDataHeader.get(groupPosition)) && reserveManageData3.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("Whether it's changing the reservation date or time, the number of people going, or any detail, " +
                            "a customer can alter a confirmed reservation by sending a request email to the establishment.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Cancel Reservation");
                    alert1.show();
                }else if(reserveManage.equals(listDataHeader.get(groupPosition)) && reserveManageData4.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("\n-In every reservation, there's a reservation status that keeps you updated (Confirmed, Cancelled or Rescheduled)," +
                            "then Tap for its details (can be found on the Reservations Feature). " +
                            "\n-At the right side, a confirmed status will appear which means you’re good to go! " +
                            "\nYou’ll receive an (SMS) Notification after the confirmation and a day before the date of reservation." +
                            "\n-Cancelled Status means that you didn't reserve a slot" +
                            "\n-Rescheduled Status means that your reservation date has been rescheduled.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Reschedule Reservation");
                    alert1.show();
                }else if(reserveManage.equals(listDataHeader.get(groupPosition)) && reserveManageData5.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("A reservation may be \"Pending\" because the establishment needs to respond to a reservation request. " +
                            "\nAn establishment has 24 hours to accept or decline the request. " +
                            "\nThe status for your reservation will be \"Pending\" until the establishments responds. " +
                            "\nDuring this time, you can also contact the establishment through email to approve your request. ")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Reservation History");
                    alert1.show();
                }else if(reserveManage.equals(listDataHeader.get(groupPosition)) && reserveManageData6.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("Upon cancelling a reservation, one must check first the status of their reservations. " +
                            "\nIf the establishment has already confirmed it, then it’s subject to their cancellation policy (cancellation can be done 3 days before the reservation date " +
                            "\n\nIf it’s still pending, you can cancel, and you won’t be charged for the reservation/transaction fees on your next reservation.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Delete Reservation");
                    alert1.show();
                }else if(promoDealsManage.equals(listDataHeader.get(groupPosition)) && promoDealsManageData1.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                    builder.setMessage("In every establishment, the cost of a certain offer, services or products may vary." +
                            "\n\nIn order to know the specific pricing and fees, you can navigate through the application, " +
                            "by clicking a specific establishment. The pricing and fees will be found on the details.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("View Promo and Deals");
                    alert1.show();
                }else if(promoDealsManage.equals(listDataHeader.get(groupPosition)) && promoDealsManageData2.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))) {
                    builder.setMessage("Pakigsa-Bot App collects a transaction fee for every transaction that will take place on the application. " +
                            "\nThe standard cost of the transaction fee is P100")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Add Promo and Deals");
                    alert1.show();
                }else if(promoDealsManage.equals(listDataHeader.get(groupPosition)) && promoDealsManageData3.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))) {
                    builder.setMessage("Paying and communicating through Airbnb helps ensure that you're protected under all of our " +
                            "Establishment and customer safeguards. You can pay the fees after you have inputted all the necessary " +
                            "details needed for the reservation. Moreover, you cannot reserve a slot on a specific establishment" +
                            " if you did not pay for the fees. Hence, you will be prompted to pay before you can proceed to reserve.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Edit Promo and Deals");
                    alert1.show();
                }else if(promoDealsManage.equals(listDataHeader.get(groupPosition)) && promoDealsManageData4.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))) {
                    builder.setMessage("You cannot request a refund on the Pakigsa-Bot App. This is being stated on the " +
                            "establishment's reservation’s cancellation policy. If your reservation has been declined " +
                            "or cancelled, you don't need to pay for the transaction fee on your next reservation.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Delete Promo and Deals");
                    alert1.show();
                }else if(estAccount.equals(listDataHeader.get(groupPosition)) && estAccountData1.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))) {
                    builder.setMessage("Deals, Discounts and Vouchers are not convertible to cash. Hence, discounts and vouchers will only " +
                            "be applicable either in free of transaction fees or acquire premium features. " +
                            "\n\nTo see the premium features, you can navigate through your profile and " +
                            "see the premium subscription details and terms.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Creating an Account");
                    alert1.show();
                }else if(estAccount.equals(listDataHeader.get(groupPosition)) && estAccountData2.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))) {
                    builder.setMessage("Signing up  or Creating an account is free. You just have to input your credentials on the Sign Up Page of " +
                            "the Pakigsa-Bot Application. Additionally, before creating an account, you have to accept the Pakigsa-Bot Community Commitment" +
                            " or Agreement.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Managing Account");
                    alert1.show();
                }else if(estAccount.equals(listDataHeader.get(groupPosition)) && estAccountData3.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))) {
                    builder.setMessage("You can edit your account details, as well as some information on your public profile, from your Profile section." +
                            "\nYou can also reset your password on the profile section. " +
                            "\n\nIn the event that you have forgotten your password, you just have to click the \"Forgot Password\", afterwhich, you will receive" +
                            " a new auto-generated password in your email. This will be found on the Sign In section.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Account Subscription");
                    alert1.show();
                }else if(estAccount.equals(listDataHeader.get(groupPosition)) && estAccountData4.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))) {
                    builder.setMessage("You can subscribe to the Pakigsa-Bot application anytime. " +
                            "\nYou can upgrade to Premium for just P459 a month. " +
                            "\n\nIt includes the SMS Notification Reminders, Exclusive deals, discounts and vouchers, Priority Pass Badge, Can reserve upto 3 or more establishments" +
                            " per day, Priority Concern Service Badge, and Parking Space Reservation. ")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Account Security");
                    alert1.show();
                }else if(safety.equals(listDataHeader.get(groupPosition)) && safetyData.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))) {
                    builder.setMessage("The application allowed providers to loyal users a Loyalty Tier that " +
                            "contained Four tiers: \n\n(1)Classic - less than 10 reservations; \n(2)Silver - 10-24 reservations; " +
                            "\n(3)Gold - 25-44 reservations; and \n(4)Platinum - 44 or more reservations. " +
                            "\nThese tiers came with different discounts and vouchers such as birthday vouchers, rewards, and 100% " +
                            "off of transaction fees. \n\nAs such, these will be provided when customers maintain their reservation status " +
                            "and complete a minimum number of reservations every six months.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("Reporting Issues");
                    alert1.show();
                }else if(pakigsabot.equals(listDataHeader.get(groupPosition)) && pakigsabotData.equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))) {
                    builder.setMessage("Here’s how you can help protect your account. \n(1)Start with a solid password: " +
                            " Your account's password should be unique—preferably completely different from any other account, " +
                            "such as your email, social media, or your bank. So, if one account is compromised, " +
                            "it's less likely to impact others. " +
                            "\n(2) Flag suspicious activity: If you think something is off about your account, profile, application activity, flag it for review." +
                            "You can email or contact the administration in the Help Center Section." +
                            "\n(3) Stay up to date: Check for software updates that that protect you from security risks." +
                            "\n(4) Always go through us: Keep all payments and communication on the Pakigsa-Bot application. " +
                            "Don't transfer funds outside Pakigsa-Bot or share your email address before a reservation is accepted. " +
                            "If someone emails you asking you to pay or accept payment offsite, contact Pakigsa-Bot administration right away.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert1 = builder.create();
                    //Setting the title manually
                    alert1.setTitle("How it Works");
                    alert1.show();
                }
                return false;
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Setup Establishment");
        listDataHeader.add("Reservation Management");
        listDataHeader.add("Promo and Deals Management");
        listDataHeader.add("Your Account");
        listDataHeader.add("Safety");
        listDataHeader.add("About Pakigsa-Bot");

        // Adding child data to Select and Reserve
        List<String> setUpEst = new ArrayList<String>();
        setUpEst.add("View");
        setUpEst.add("Add");
        setUpEst.add("Edit");
        setUpEst.add("Delete");

        // Adding child data to Reservations
        List<String> reservationManagement = new ArrayList<String>();
        reservationManagement.add("View Reservations");
        reservationManagement.add("Accept/Confirm Reservation");
        reservationManagement.add("Cancel Reservation");
        reservationManagement.add("Reschedule Reservation");
        reservationManagement.add("Reservation History");
        reservationManagement.add("Delete Reservation");

        // Adding child data to Payments, Price, Refunds
        List<String> promoDealsManagement = new ArrayList<String>();
        promoDealsManagement.add("View Promo and Deals");
        promoDealsManagement.add("Add Promo and Deals");
        promoDealsManagement.add("Edit Promo and Deals");
        promoDealsManagement.add("Delete Promo and Deals");

        // Adding child data to Account
        List<String> establishmentAccount = new ArrayList<String>();
        establishmentAccount.add("Creating an Account");
        establishmentAccount.add("Managing Account");
        establishmentAccount.add("Account Subscription");
        establishmentAccount.add("Account Security");

        // Adding child data to Safety
        List<String> safety = new ArrayList<String>();
        safety.add("Reporting issues");;

        // Adding child data to Pakigsabot
        List<String> pakigsabot = new ArrayList<String>();
        pakigsabot.add("How it Works");

        // Header, Child data
        listDataChild.put(listDataHeader.get(0), setUpEst);
        listDataChild.put(listDataHeader.get(1), reservationManagement);
        listDataChild.put(listDataHeader.get(2), promoDealsManagement);
        listDataChild.put(listDataHeader.get(3), establishmentAccount);
        listDataChild.put(listDataHeader.get(4), safety);
        listDataChild.put(listDataHeader.get(5), pakigsabot);
    }

    private void refs(){
        imgBackBtn = findViewById(R.id.imgBackBtn);
        gmailBtn = findViewById(R.id.gmailBtn);
    }

    private void fragments(){
        setContentView(R.layout.activity_bottom_navigation);
        //Bottom Nav
        //Assign variable
        bottomNavigation = findViewById(R.id.bottom_nav);

        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_reservations));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_services));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_history));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.ic_help));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;

                //Check condition
                switch (item.getId()){
                    case 1: //When id is 1, initialize nearby fragment
                        fragment = new ReservationsFragment();
                        break;

                    case 2: //When id is 2, initialize reservations fragment
                        fragment = new ServicesFragment();
                        break;

                    case 3: //When id is 3, initialize home fragment
                        fragment = new HomeFragment();
                        break;

                    case 4: //When id is 4, initialize favorites fragment
                        fragment = new HistoryFragment();
                        break;

                    case 5: //When id is 5, initialize help center fragment
                        fragment = new HelpCenterFragment();
                        break;
                }
                //Load fragment
                loadFragment(fragment);
            }
        });

        /*//Set notification count
        bottomNavigation.setCount(3,"10");*/
        //Set help center fragment initially selected
        bottomNavigation.show(5,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        //Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
    }

    private void email(){
        Intent intent = new Intent(getApplicationContext(), EmailMessageLayout.class);
        startActivity(intent);
    }
}