package com.geekchun.client;
哈哈哈
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    //客户端
    private MqttAndroidClient client;

    //连接选项
    private MqttConnectOptions options;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client_init();//注意：这里不能调用订阅和发布的相关方法，会导致闪退
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe("geekchun");//暂时只能在其他函数内调用发布订阅的相关函数，才学尚浅，暂时无解
            }
        });
    }
    private void client_init()
    {
        //初始化客户端对象,第二个参数就是MQTT服务器的地址和端口，更换服务器只需改动这里即可
        client=new MqttAndroidClient(this,"tcp://39.97.103.146:1883","android");
        options=new MqttConnectOptions();
        //设置连接选项
        options.setCleanSession(true);
        options.setKeepAliveInterval(10);
        options.setConnectionTimeout(50);
        options.setAutomaticReconnect(true);
        options.setUserName("geekchun");
        options.setPassword("geekchun".toCharArray());

        //设置回调函数
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //连接丢失的回调函数
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //收到消息的回调函数
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        //开始连接服务器
            try {
                client.connect(options, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //连接成功
                        if (client.isConnected())Toast.makeText(MainActivity.this,"2641",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        //连接失败
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }


    }
    private boolean subscribe(String topic)
    {
        if(client.isConnected())
        {
            try {
                client.subscribe(topic, 0, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //订阅成功
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        //订阅失败
                    }
                });
            } catch (MqttException e) {

                return false;
            }

        }
        else {
            //未连接服务器
            return  false;
        }
            return true;
    }
    private boolean publish(String topic,String message)
    {
        MqttMessage mqttMessage=new MqttMessage();
        mqttMessage.setPayload(message.getBytes());
        try {
            client.publish(topic, mqttMessage, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //发布成功
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //发布失败
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            return  false;
        }
        return true;
    }
}
