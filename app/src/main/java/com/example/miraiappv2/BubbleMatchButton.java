package com.example.miraiappv2;

import org.json.JSONException;
import org.json.JSONObject;

public class BubbleMatchButton {
        //Declare strings to store the id, the english word, the japanese word, sound, type and topic.
        String id, eword, jword, topic, type, sound;

        private boolean matched;

        //Constructor that sets all the fields of a BubbleMatchButton object.
        public BubbleMatchButton(String id, String eword, String jword, String topic, String type, String sound){
            this.id = id;
            this.eword = eword;
            this.jword = jword;
            this.topic = topic;
            this.type = type;
            this.sound = sound;
            this.matched = false;
        }

        //Get methods for accessing the fields of a BubbleMatchButton
        public String getId() {
            return id;
        }

        public String getEword() {
            return eword;
        }

        public String getJword() {
            return jword;
        }

        public String getTopic() {
            return topic;
        }

        public String getType() {
            return type;
        }

        public String getSound() {
            return sound;
        }

        public boolean isMatched() {
            return matched;
        }

        public void setMatched(boolean matched) {
            this.matched = matched;
        }

        public String getJSONValue() {
            //Create a JSON object for the button data to be used in other file
            JSONObject jsonObject = new JSONObject();
            try {
                //Add the data to the JSON object
                jsonObject.put("id", id);
                jsonObject.put("eword", eword);
                jsonObject.put("jword", jword);
                jsonObject.put("topic", topic);
                jsonObject.put("type", type);
                jsonObject.put("sound", sound);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Return the object as a string
            return jsonObject.toString();
        }
}
