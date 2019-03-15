package Main;
import Effects.BlackWhiteEffect;
import Effects.GaussianBlurEffect;
import Tools.PaintDraw;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ToolbarView {
	AnchorPane toolbarPane;
	EditingView editingView = new EditingView();

	public ToolbarView(AnchorPane pane) {
		toolbarPane = pane;
		GetToolbarView();
	}
	
	public void GetToolbarView() {
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		Button btnGaussianBlur = new Button("Gaussian Blur");
		Button btnBlackAndWhite = new Button("Black and White");

		btnGaussianBlur.setOnAction((event) -> {
			GaussianBlurEffect gaussianBlurEffect = new GaussianBlurEffect(editingView.imageViewEditView.getImage(), 45, 0);
			editingView.imageViewEditView.setImage(gaussianBlurEffect.getEffect());
		});

		Slider sliderGaussian = new Slider (0, 100, 100);
		sliderGaussian.setMin(0);
		sliderGaussian.setValue(0);
		sliderGaussian.setMax(100);
		sliderGaussian.setMajorTickUnit(5);
		sliderGaussian.setMinorTickCount(0);
		sliderGaussian.setShowTickMarks(true);
		sliderGaussian.setShowTickLabels(true);
		sliderGaussian.setSnapToTicks(true);
		Label gaussianScale = new Label();

		PauseTransition pause = new PauseTransition(Duration.millis(50));

		// Gaussian Bar event. The Gaussian filter listen to the bar value change.
		sliderGaussian.valueProperty().addListener( (observable, oldValue, newValue) -> {

			pause.setOnFinished(event -> {

				Runnable runGaussian = ()->{
					int kernel = newValue.intValue();
					if (kernel % 2 == 0){
						kernel += 1;
					}

					GaussianBlurEffect gaussianBlurEffect =
							new GaussianBlurEffect(editingView.imgSetByNewDrag.getImage(), kernel, 0);

					editingView.imageViewEditView.setImage(gaussianBlurEffect.getEffect());
				};

				new Thread(runGaussian).start();
			});
			pause.playFromStart();
		});


		// Gaussian Bar event - when mouse on release
		sliderGaussian.setOnMouseReleased(event -> {
				int kernel = (int) sliderGaussian.getValue();
				if (kernel % 2 == 0){
					kernel += 1;
				}
				GaussianBlurEffect gaussianBlurEffect =
						new GaussianBlurEffect(editingView.imgSetByNewDrag.getImage(), kernel, 0);

				gaussianBlurEffect.setGaussianEffect(kernel);
				editingView.imageViewEditView.setImage(gaussianBlurEffect.getEffect());

		});

		btnBlackAndWhite.setOnAction((event) -> {
			BlackWhiteEffect blackWhiteEffect = new BlackWhiteEffect(editingView.imageViewEditView.getImage());
			editingView.imageViewEditView.setImage(blackWhiteEffect.getEffect());
		});


		Button Draw = new Button("Draw");
		Slider slider = new Slider (0, 100, 100);
		ColorPicker colorPicker = new ColorPicker();

		gp.add(btnGaussianBlur, 0, 0);
		gp.add(sliderGaussian, 0, 1);
		gp.add(gaussianScale, 1,1);
		gp.add(btnBlackAndWhite, 0, 2);
		gp.add(Draw, 0, 3);
		gp.add(slider, 0, 4);
		gp.add(colorPicker, 0, 5);
		toolbarPane.getChildren().addAll(gp);
	}
}
