package pf.bb.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import pf.bb.Main;
import pf.bb.controller.BuilderController;
import pf.bb.model.Configuration;
import pf.bb.model.User;

import static pf.bb.controller.LoginController.activeUser;

/**
 * Klasse für das Auslagern des SaveConfigurationTasks.
 * @author Stephan Kost
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public class SaveConfigurationTask extends Task<Configuration> {

    private final User user;
    private final BuilderController bc;

    private final String status;

    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

    public SaveConfigurationTask(User user, BuilderController bc, String status) {
        this.user = user;
        this.bc = bc;
        this.status = status;
    }

    @Override
    protected Configuration call() throws Exception {

        bc.finalArticleIdArray = FXCollections.observableIntegerArray();
        bc.finalArticleIdArray.addAll(
                bc.getArticleIDByNameSizeAndColor(bc.cat1SelectName.getValue(), bc.svgInfoFrameSize, bc.svgInfoFrameColorHex), // 0=Rahmen
                bc.getArticleIDByNameAndColor(bc.cat2SelectModel.getValue(), bc.svgInfoHandlebarColorHex), // 1=Lenker
                bc.getArticleIdByName(bc.cat2SelectGrip.getValue()), //2=Griffe
                bc.getArticleIDByNameSizeAndColor(bc.cat3SelectModel.getValue(), bc.svgInfoWheelsSize, bc.svgInfoWheelsColorHex), //3=Räder
                bc.getArticleIdByName(bc.cat3SelectTyre.getValue()), //4=Reifen
                bc.getArticleIDByNameAndColor(bc.cat4SelectModel.getValue(), bc.svgInfoSaddleColorHex), //5=Sattel
                bc.getArticleIdByName(bc.cat5SelectModel.getValue()), //6=Bremsen
                bc.getArticleIdByName(bc.cat6SelectBell.getValue()), //7=Klingel
                bc.getArticleIdByName(bc.cat6SelectStand.getValue()), //8=Ständer
                bc.getArticleIdByName(bc.cat6SelectLight.getValue()) //9=Licht
        );

        System.out.println("BuilderController: Article-ID Collection = " + bc.finalArticleIdArray);

        int decrement = 1;
        Configuration configNew = new Configuration(activeUser);
        configNew.articles.add(Main.ARTICLES.get(bc.finalArticleIdArray.get(0) - decrement)); // Rahmen
        configNew.articles.add(Main.ARTICLES.get(bc.finalArticleIdArray.get(1) - decrement)); // Lenker
        configNew.articles.add(Main.ARTICLES.get(bc.finalArticleIdArray.get(2) - decrement)); // Griffe
        configNew.articles.add(Main.ARTICLES.get(bc.finalArticleIdArray.get(3) - decrement)); // Räder
        configNew.articles.add(Main.ARTICLES.get(bc.finalArticleIdArray.get(4) - decrement)); // Reifen
        configNew.articles.add(Main.ARTICLES.get(bc.finalArticleIdArray.get(5) - decrement)); // Sattel
        configNew.articles.add(Main.ARTICLES.get(bc.finalArticleIdArray.get(6) - decrement)); // Bremsen
        configNew.articles.add(Main.ARTICLES.get(bc.finalArticleIdArray.get(7) - decrement)); // Klingel
        configNew.articles.add(Main.ARTICLES.get(bc.finalArticleIdArray.get(8) - decrement)); // Ständer
        configNew.articles.add(Main.ARTICLES.get(bc.finalArticleIdArray.get(9) - decrement)); // Licht
        System.out.println("BuilderController: Article-ID config object = " + configNew.articles);

        try {
            String configJSON;
            if (Main.currentConfig == null) {
                configNew.status = this.status;
                configJSON = gson.toJson(configNew);

                HttpResponse<JsonNode> res = Unirest.post(Configuration.getUrl()).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(configJSON).asJson();
                String json = res.getBody().toString();
                Configuration tempConfig = new Gson().fromJson(json, Configuration.class);
                // Add db-generated values to Object
                configNew.id = tempConfig.id;
                configNew.timestampCreated = tempConfig.timestampCreated;
                configNew.timestampLastTouched = tempConfig.timestampLastTouched;
                return configNew;

            } else {
                // Artikelliste der bestehenden Konfiguration aktualisieren
                Main.currentConfig.articles = configNew.articles;
                Main.currentConfig.status = this.status;
                configJSON = gson.toJson(Main.currentConfig);
                String url = Configuration.getUrl() + "/" + Main.currentConfig.id;
                System.out.println("Sending put request to: " + url);

                HttpResponse<JsonNode> res = Unirest.put(url).header("Content-Type", "application/json").header("Authorization", user.jsonWebToken).body(configJSON).asJson();

                if(res.getStatus() == 403) { //FORBIDDEN
                    System.out.println("WRITE ACCESS TO CONFIGURATION DENIED, you must get write access via PutConfigurationWriteAccessTask before you can run a PutConfigurationTask!");
                }
                //System.out.println("Answer to PUT body:" + res.getHeaders() + "\n" + res.getBody());
                return Main.currentConfig;
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
