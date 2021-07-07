package com.example.gasstation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CheckEmailActivity extends AppCompatActivity {
EditText chek_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_email);
        chek_email=findViewById(R.id.check_email_address);
    }

    public void CheckEmail(View view) {
Database db=new Database();
db.connect(this);
        Random r=new Random();
        StaticsClass.vCodeCheckEmail=r.nextInt(9999-1111+1)+1111;
        ResultSet rs=db.GetData("select * from Customer where Email='"+chek_email.getText()+"'");
        try {
            if(rs.next())
            {

                StaticsClass.nameCheckEmail=rs.getString("CustName");
                StaticsClass.emailCheckEmail=chek_email.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            final String username = "gasstationeg@gmail.com";
                            final String password = "153426Qw!";
                            Properties props = new Properties();
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.starttls.enable", "true");
                            props.put("mail.smtp.host", "smtp.gmail.com");
                            props.put("mail.smtp.port", "587");

                            Session session = Session.getInstance(props,
                                    new Authenticator() {
                                        protected PasswordAuthentication getPasswordAuthentication() {
                                            return new PasswordAuthentication(username, password);
                                        }
                                    });

                            try {
                                Message message = new MimeMessage(session);
                                message.setFrom(new InternetAddress("gasstationeg@gmail.com"));
                                message.setRecipients(Message.RecipientType.TO,
                                        InternetAddress.parse(chek_email.getText().toString()));
                                message.setSubject("Forget Password  - Gas Station App-");
                                message.setText("Dear : "+rs.getString("CustName")+"Your Activation code is : "+StaticsClass.vCodeCheckEmail);
                                Transport.send(message);

                            } catch (MessagingException e) {
                                Toast.makeText(getApplication(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                                throw new RuntimeException(e);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(getApplication(), "Activation code has been sent Check your Email", Toast.LENGTH_LONG).show();

startActivity(new Intent(CheckEmailActivity.this,VerficationActivity.class));
finish();
            }
else {
                Toast.makeText(this,"Invalid Email", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
    }
}