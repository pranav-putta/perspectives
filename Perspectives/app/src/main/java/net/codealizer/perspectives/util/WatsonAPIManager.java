package net.codealizer.perspectives.util;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.DocumentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.SentenceAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneInput;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import java.util.List;

public class WatsonAPIManager {

    private static final String VERSION = "version-2017-09-21";
    private static final String USERNAME = "ec24a73d-2edd-4f97-bb4f-e2280b898850";
    private static final String PASSWORD = "kAvNtzHgrJOo";

    private ToneAnalyzer mAPI;

    private WatsonAPIManager() {
        mAPI = new ToneAnalyzer(VERSION, USERNAME, PASSWORD);
    }

    public static WatsonAPIManager getInstance() {
        return new WatsonAPIManager();
    }

    /**
     * Runs an API request on thread
     * @param text input text
     * @return analysis of tone for text
     */
    private ToneAnalysis analyze(String text) {
        ToneOptions toneOptions = new ToneOptions.Builder()
                .text(text)
                .sentences(true)
                .build();
        ToneAnalysis analysis = mAPI.tone(toneOptions).execute();

        return analysis;
    }


}
