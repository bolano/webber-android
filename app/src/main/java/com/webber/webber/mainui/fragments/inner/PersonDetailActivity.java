package com.webber.webber.mainui.fragments.inner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webber.webber.R;
import com.webber.webber.db.Person;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;

public class PersonDetailActivity extends Activity {

    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        Bundle extras = getIntent().getExtras();
        this.person = extras.getParcelable("person");

        Log.v("address", person.getAddress());

        initCard();
    }

    protected void initCard() {

        PersonCard card = new PersonCard(this);
        card.init();

        card.setCompany(person.getCompany());
        card.setDivision(person.getDivision());
        card.setAddress(person.getAddress());
        card.setPersonName(person.getRealname());
        card.setCellphone(person.getCellphone());
        card.setEmail(person.getEmail());
        card.setTelephone(person.getTelephone());
        card.setPosition(person.getPosition());

        CardView cardView = (CardView) findViewById(R.id.card_person_detail);
        cardView.setCard(card);

    }

    public class PersonCard extends Card {

        protected TextView mPersonName;
        protected TextView mCompany;
        protected TextView mDivision;
        protected TextView mPosition;
        protected TextView mAddress;
        protected TextView mCellPhone;
        protected TextView mTelephone;
        protected TextView mEMail;

        protected int resourceIdThumbnail;

        protected String person_name;
        protected String company;
        protected String division;
        protected String position;
        protected String address;

        protected String cellphone;
        protected String telephone;
        protected String email;

        public PersonCard(Context context) {
            this(context, R.layout.person_detail_card);
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
            mPersonName = (TextView) parent.findViewById(R.id.person_detail_card_realname);
            mCompany = (TextView) parent.findViewById(R.id.person_detail_card_company);
            mDivision = (TextView) parent.findViewById(R.id.person_detail_card_division);
            mPosition = (TextView) parent.findViewById(R.id.person_detail_card_position);
            mAddress = (TextView) parent.findViewById(R.id.person_detail_card_address);
            mCellPhone = (TextView) parent.findViewById(R.id.person_detail_card_cellphone);
            mTelephone = (TextView) parent.findViewById(R.id.person_detail_card_telephone);
            mEMail = (TextView) parent.findViewById(R.id.person_detail_card_email);

            mPersonName.setText(person_name);
            mCompany.setText(company);
            mDivision.setText(division);
            mPosition.setText(position);
            mAddress.setText(address);
            mCellPhone.setText(cellphone);
            mTelephone.setText(telephone);
            mEMail.setText(email);
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

        public void setAddress(String address) {
            this.address = address;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public void setEmail(String email) {
            this.email = email;
        }


        public int getResourceIdThumbnail() {
            return resourceIdThumbnail;
        }

        public void setResourceIdThumbnail(int resourceIdThumbnail) {
            this.resourceIdThumbnail = resourceIdThumbnail;
        }
    }

}
