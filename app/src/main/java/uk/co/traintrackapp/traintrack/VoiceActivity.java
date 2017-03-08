package uk.co.traintrackapp.traintrack;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;

import uk.co.traintrackapp.traintrack.utils.Utils;

@TargetApi(23)
public class VoiceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.log("onCreate: ");

    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.log("onResume: ");
        Intent intent = getIntent();

        if (intent == null) {
            Utils.log("intent is null");
            finish();
            return;
        }

        String query = intent.getStringExtra(SearchManager.QUERY);
        Utils.log("onResume: Searching for: " + query);

        Utils.log("onResume: isVoiceInteraction: " + isVoiceInteraction());
        Utils.log("onResume: isVoiceInteractionRoot: " + isVoiceInteractionRoot());

        if (isVoiceInteraction()) {
            //One option can have many synonyms
            VoiceInteractor.PickOptionRequest.Option voiceOption1 =
                    new VoiceInteractor.PickOptionRequest.Option("Green", 1);
            voiceOption1.addSynonym("Olive");
            voiceOption1.addSynonym("Emerald");

            VoiceInteractor.PickOptionRequest.Option voiceOption2 =
                    new VoiceInteractor.PickOptionRequest.Option("Red", 1);
            voiceOption2.addSynonym("Crimson");
            voiceOption2.addSynonym("Burgundy");

            //Add as many options as you’d like within the option array, this will increase the chances of //a successful response.
            getVoiceInteractor()
                    .submitRequest(new VoiceInteractor.PickOptionRequest(new VoiceInteractor.Prompt(new String[]{"What is your favorite color?"}, "What is your favorite color?"), new VoiceInteractor.PickOptionRequest.Option[]{voiceOption1, voiceOption2}, null) {
                        @Override
                        public void onPickOptionResult(boolean finished, Option[] selections, Bundle result) {
                            if (finished && selections.length == 1) {
                                //Use the index of the options array to determine what was said
                                selections[0].getIndex();
                            }
                        }

                        @Override
                        public void onCancel() {
                            getActivity().finish();
                        }
                    });
        }
        finish();
    }

}
