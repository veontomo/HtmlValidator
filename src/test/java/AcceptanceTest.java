import com.veontomo.htmlvalidator.Views.MainView;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;


import static org.testfx.api.FxAssert.verifyThat;

/**
 * Test suite for  GUI of the app.
 */
public class AcceptanceTest extends ApplicationTest {
    /**
     * ids of the menu items as they are defined in the fxml layout file with prepended "#"
     */
    private final String MENU_FILE_ID = "#" + "menuFile";
    private final String MENU_SELECT_ID = "#" + "menuSelect";
    private final String MENU_ANALYZE_ID = "#" + "menuAnalyze";
    private final String MENU_CLEAR_ID = "#" + "menuClear";
    private Stage mStage;

    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(new MainView(stage).getScene());
        mStage = stage;
        stage.show();


    }

    /**
     * Test the functionality of the menu item that is responsible for opening a file
     * Partition as follows:
     * 1. it is a first action: true, false
     */
    // Cover:
    // 1. it is a first action: true
    @Test
    public void menu_select_should_be_enabled_upon_start() {
        clickOn(MENU_FILE_ID);
        verifyThat(MENU_SELECT_ID, (Node node) -> !node.isDisable());
    }

    // Cover:
    // 1. it is a first action: false
    @Test
    @Ignore
    public void menu_select_is_enabled_after_selecting() throws InterruptedException {
        clickOn(MENU_FILE_ID).clickOn(MENU_SELECT_ID);
//        type(KeyCode.M);// MECSPE_2016_03_01.html
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn(MENU_FILE_ID);
        verifyThat(MENU_SELECT_ID, (Node node) -> !node.isDisable());
    }

    /**
     * Test the functionality of the menu that analyze the file.
     * Partition as follows:
     * 1. it is a first action: true, false
     */
    // Cover:
    // 1. it is a first action: true
    @Test
    @Ignore
    public void menu_analyze_should_be_disabled_upon_start() {
        clickOn(MENU_FILE_ID);
        verifyThat(MENU_ANALYZE_ID, Node::isDisable);
    }

    /**
     * Test the functionality of the menu that clear the results.
     * Partition as follows:
     * 1. it is a first action: true, false
     */
    // Cover:
    // 1. it is a first action: true
    @Test
    @Ignore
    public void menu_clear_should_be_disabled_upon_start() {
        clickOn(MENU_FILE_ID);
        verifyThat(MENU_CLEAR_ID, Node::isDisable);
    }


}
