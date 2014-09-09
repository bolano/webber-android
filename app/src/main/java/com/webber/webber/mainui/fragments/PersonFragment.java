package com.webber.webber.mainui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webber.webber.R;
import com.webber.webber.client.JSONfunctions;
import com.webber.webber.db.Person;
import com.webber.webber.db.Relation;
import com.webber.webber.db.WebberDBOpenHelper;
import com.webber.webber.mainui.WebberActivity;
import com.webber.webber.mainui.fragments.inner.PersonDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import static com.webber.webber.general.PreferenceManager.checkLastUpdate;
import static com.webber.webber.general.PreferenceManager.getLastUpdate;
import static com.webber.webber.general.PreferenceManager.setLastUpdate;

public class PersonFragment extends Fragment {

    public static String TAG = "PersonFragment";
    public static String REALNAME = "realname";
    public static String PID = "pid";
    public static String COMPANY = "company";
    public static String DIVISION = "division";
    public static String POSITION = "position";
    public static String NEWS_TITLE = "news_title";
    public static String PHOTO_URL = "photo_url";
    public static String NEWS_UPDATE = "news_date";
    public static String ADDRESS = "address";
    public static String CELLPHONE = "cellphone";
    public static String TELEPHONE = "telephone";
    public static String EMAIL = "email";
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> personData;
    JSONObject jsonobject;
    JSONArray jsonarray;
    WebberDBOpenHelper wdb;
    HashMap<String, Integer> pidIndex;
    String uid = null;
    String realname = null;

    public PersonFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            WebberActivity activity = (WebberActivity) getActivity();
            this.uid = activity.uid;
            this.realname = activity.realname;

            this.wdb = new WebberDBOpenHelper(this.getActivity());

            View rootView = inflater.inflate(R.layout.fragment_person, container, false);

            //try to build personData from database

            //TODO: only show the top ones
            //TODO: pull to refresh

            //check if it is necessary to sync the server
            if (checkLastUpdate(this.getActivity()) == false) {
                new DownloadJSON().execute();
            } else {
                new InitFromDB().execute();
            }

            return rootView;
        } catch (Exception e) {
            Log.v("debug", e.toString());
        }

        return null;

    }

    private void initFromDB() {
        personData = new ArrayList<HashMap<String, String>>();

        List<Person> personList = wdb.getAllFriends(uid);

        Log.v(TAG, String.valueOf(personList.size()));

        for (int i = 0; i < personList.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            Person person = personList.get(i);
            // Retrive JSON Objects
            map.put("realname", person.getRealname());
            map.put("company", person.getCompany());
            map.put("division", person.getDivision());
            map.put("position", person.getPosition());
            map.put("news_title", person.getNews_title());
            map.put("news_date", person.getNews_date());
            map.put("photo_url", person.getPhoto_url());
            map.put("address", person.getAddress());
            map.put("cellphone", person.getCellphone());
            map.put("telephone", person.getTelephone());
            map.put("email", person.getEmail());
            map.put("pid", person.getPid());
            // Set the JSON Objects into the array
            personData.add(map);
        }

        buildPIDIndex();
    }

    private void buildPIDIndex() {
        pidIndex = new HashMap<String, Integer>();
        for (int i = 0; i < personData.size(); i++) {
            pidIndex.put(personData.get(i).get("pid"), i);
        }
    }

    private void updateDBFromServerData(ArrayList<HashMap<String, String>> personServerData) {
        for (int i = 0; i < personServerData.size(); i++) {
            HashMap<String, String> personMap = personServerData.get(i);
            Person person = new Person(
                    personMap.get("pid"),
                    personMap.get("realname"),
                    personMap.get("company"),
                    personMap.get("division"),
                    personMap.get("position"),
                    personMap.get("news_date"),
                    personMap.get("news_title"),
                    personMap.get("address"),
                    personMap.get("cellphone"),
                    personMap.get("telephone"),
                    personMap.get("email"));
            //update or insert
            if (wdb.getPerson(person.getPid()) != null) {
                wdb.updatePerson(person);
            } else {
                wdb.addPerson(person);
            }

            //update relationship
            Relation rel = new Relation();
            rel.setPid(person.getPid());
            rel.setUid(uid);
            wdb.addRelation(rel);
        }
    }

    private void updateDataFromServer(ArrayList<HashMap<String, String>> personServerData) {
        //merge data from server with data from local db
        for (int i = 0; i < personServerData.size(); i++) {
            String pid = personServerData.get(i).get("pid");
            if (pidIndex.containsKey(pid)) {
                int index = pidIndex.get(pid);
                personData.set(index, personServerData.get(i));
            } else {
                //simply add the person to list
                personData.add(personServerData.get(i));
            }
        }

        //sort personData
        Collections.sort(personData, new MapComparator("news_date"));
    }


    protected void initCards() {

        //Init an array of Cards
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < personData.size(); i++) {
            PersonCard card = new PersonCard(this.getActivity());
            card.init();

            final Person person = new Person(personData.get(i).get(PID),
                    personData.get(i).get(REALNAME),
                    personData.get(i).get(COMPANY),
                    personData.get(i).get(DIVISION),
                    personData.get(i).get(POSITION),
                    personData.get(i).get(NEWS_UPDATE),
                    personData.get(i).get(NEWS_TITLE),
                    personData.get(i).get(ADDRESS),
                    personData.get(i).get(CELLPHONE),
                    personData.get(i).get(TELEPHONE),
                    personData.get(i).get(EMAIL)
            );


            card.setCompany(person.getCompany());
            card.setDivision(person.getDivision());
            card.setNewsDate(person.getNews_date());
            card.setPersonName(person.getRealname());
            card.setNewsTitle(person.getNews_title());
            card.setPosition(person.getPosition());

            card.setOnClickListener(
                    new Card.OnCardClickListener() {
                        @Override
                        public void onClick(Card card, View view) {
                            //Toast.makeText(getContext(), "Click Listener card=" + person_name, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), PersonDetailActivity.class);

                            intent.putExtra("person", person);

                            getActivity().startActivity(intent);
                        }
                    }
            );

            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        CardListView listView = (CardListView) getActivity().findViewById(R.id.cardlist_person);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    // Init from DB AsyncTask
    private class InitFromDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Loading friends...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initFromDB();
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            try {
                initCards();
                // Close the progressdialog
                mProgressDialog.dismiss();
            } catch (Exception e) {
                Log.e("Webber", "exception", e);
            }
        }
    }

    // DownloadJSON AsyncTask
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Loading friends...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            initFromDB();

            String uid = PersonFragment.this.uid;
            // Create an array
            ArrayList<HashMap<String, String>> personServerData = new ArrayList<HashMap<String, String>>();
            // Retrieve JSON Objects from the given URL address
            //TODO: send lastsynctime to sever
            long lastUpdate = getLastUpdate(getActivity());
            long thisUpdate = System.currentTimeMillis();
            //Log.v(TAG, String.valueOf(lastUpdate));
            jsonobject = JSONfunctions
                    .getJSONfromURL("http://101.6.30.217:8866/friend?uid=" + uid + "&last=" + String.valueOf(lastUpdate));
            //Log.v("debug", "http://101.6.30.217:8866/friend?uid="+uid);

            try {
                // Locate the array name in JSON
                jsonarray = jsonobject.getJSONArray("friends");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects
                    map.put("realname", jsonobject.getString("realname"));
                    map.put("company", jsonobject.getString("company"));
                    map.put("division", jsonobject.getString("division"));
                    map.put("position", jsonobject.getString("position"));
                    map.put("news_title", jsonobject.getString("news_title"));
                    map.put("news_date", jsonobject.getString("news_date"));
                    map.put("photo_url", jsonobject.getString("photo_url"));
                    map.put("address", jsonobject.getString("address"));
                    map.put("cellphone", jsonobject.getString("cellphone"));
                    map.put("telephone", jsonobject.getString("telephone"));
                    map.put("email", jsonobject.getString("email"));
                    map.put("pid", jsonobject.getString("pid"));

                    // Set the JSON Objects into the array
                    personServerData.add(map);
                }

                //update database based on the received data
                updateDBFromServerData(personServerData);

                //merge server data with local data
                updateDataFromServer(personServerData);

                //recond new update time
                setLastUpdate(getActivity(), thisUpdate);

            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                Log.e("debug", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            try {
                initCards();
                // Close the progressdialog
                mProgressDialog.dismiss();
            } catch (Exception e) {
                Log.e("Webber", "exception", e);
            }
        }
    }

    public class PersonCard extends Card {

        protected TextView mPersonName;
        protected TextView mCompany;
        protected TextView mDivision;
        protected TextView mPosition;
        protected TextView mNewsTitle;
        protected TextView mNewsDate;

        protected int resourceIdThumbnail;

        protected String person_name;
        protected String company;
        protected String division;
        protected String position;
        protected String news_title;
        protected String news_date;

        public PersonCard(Context context) {
            this(context, R.layout.person_card_inner);
        }

        public PersonCard(Context context, int innerLayout) {
            super(context, innerLayout);
            //init();
        }

        private void init() {

            //Add thumbnail
            CardThumbnail cardThumbnail = new CardThumbnail(mContext);

            if (resourceIdThumbnail == 0)
                cardThumbnail.setDrawableResource(R.drawable.ic_contact);
            else {
                cardThumbnail.setDrawableResource(resourceIdThumbnail);
            }

            addCardThumbnail(cardThumbnail);

        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            //Retrieve elements
            mPersonName = (TextView) parent.findViewById(R.id.person_card_realname);
            mCompany = (TextView) parent.findViewById(R.id.person_card_company);
            mDivision = (TextView) parent.findViewById(R.id.person_card_division);
            mPosition = (TextView) parent.findViewById(R.id.person_card_position);
            mNewsTitle = (TextView) parent.findViewById(R.id.person_card_news_title);
            mNewsDate = (TextView) parent.findViewById(R.id.person_card_news_date);

            mPersonName.setText(person_name);
            mCompany.setText(company);
            mDivision.setText(division);
            mPosition.setText(position);
            mNewsTitle.setText(news_title);
            mNewsDate.setText(news_date);
        }

        public void setPersonName(String person_name) {
            this.person_name = person_name;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public void setNewsTitle(String news_title) {
            this.news_title = news_title;
        }

        public void setNewsDate(String news_date) {
            this.news_date = news_date;
        }


        public int getResourceIdThumbnail() {
            return resourceIdThumbnail;
        }

        public void setResourceIdThumbnail(int resourceIdThumbnail) {
            this.resourceIdThumbnail = resourceIdThumbnail;
        }
    }

    class MapComparator implements Comparator<HashMap<String, String>> {
        private final String key;

        public MapComparator(String key) {
            this.key = key;
        }

        public int compare(HashMap<String, String> first,
                           HashMap<String, String> second) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date firstDate = simpleDateFormat.parse(first.get(key));
                Date secondDate = simpleDateFormat.parse(second.get(key));
                return secondDate.compareTo(firstDate);
            } catch (ParseException ex) {
                Log.v(TAG, ex.getMessage());
            }

            return 0;
        }
    }

}
