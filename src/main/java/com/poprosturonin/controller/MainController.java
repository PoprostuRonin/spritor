/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.components.AboutDialog;
import com.poprosturonin.components.GenerateDialog;
import com.poprosturonin.components.NewProjectDialog;
import com.poprosturonin.components.SaveWorkDialog;
import com.poprosturonin.model.Generator;
import com.poprosturonin.model.Main;
import com.poprosturonin.model.ProjectLoader;
import com.poprosturonin.model.ProjectWriter;
import com.poprosturonin.model.design.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane assetsPane;
    @FXML
    private StackPane canvasPane;
    @FXML
    private AnchorPane designPane;

    private AssetsController assetsController;
    private CanvasController canvasController;
    private DesignController designController;

    private Generator generator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assetsController = loadAssetsPane();
        canvasController = loadCanvasPane();
        designController = loadDesignPane();

        if (designController != null && assetsController != null && canvasController != null) {
            assetsController.setDesignController(designController);
            canvasController.setDesignController(designController);
            designController.setCanvasController(canvasController);
        }
    }

    @FXML
    /** File -> New */
    private void newButton() {
        if(!designController.hasProject()) {
            NewProjectDialog dialog = new NewProjectDialog();
            Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
            if (result.isPresent() && result.get() != null) {
                Pair<Integer, Integer> pair = result.get();
                designController.setProject(new Project(pair.getKey(), pair.getValue()));
            }
        } else {
            closeProjectGently();
        }
    }

    @FXML
    /** File -> Open */
    private void openButton() {
        if(designController.hasProject()) {
            closeProjectGently();
        }
        else
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open project");
            fileChooser.setInitialFileName("project");

            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Spritor projects", "*.xml");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setSelectedExtensionFilter(extensionFilter);
            File file = fileChooser.showOpenDialog(Main.stage);

            //If user selected something
            if(file != null) {
                ProjectLoader projectLoader = new ProjectLoader(file);
                if (projectLoader.hasLoaded())
                    designController.setProject(projectLoader.getProject());
            }
        }
    }

    @FXML
    /** File -> Save */
    private void saveButton() {
        if(designController.hasProject()) saveProject();
        else Utility.errorAlert("There is no currently opened project.");
    }

    @FXML
    /** File -> Save As */
    private void saveAsButton() {
        if(designController.hasProject()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save project");
            fileChooser.setInitialFileName("project");

            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Spritor projects", "*.xml");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setSelectedExtensionFilter(extensionFilter);
            File file = fileChooser.showSaveDialog(Main.stage);

            if (file != null)
                saveProject(file);
        } else
            Utility.errorAlert("There is no currently opened project.");
    }

    @FXML
    /** File -> Close */
    private void closeButton() {
        if(designController.hasProject()) closeProjectGently();
        else Utility.errorAlert("There is no currently opened project.");
    }

    @FXML
    /** Edit -> Delete current element */
    private void deleteCurrentElementButton() {
        designController.deleteCurrentElement();
    }

    @FXML
    /** Help -> About */
    private void aboutButton() {
        AboutDialog aboutDialog = new AboutDialog();
        aboutDialog.showAndWait();
    }

    /** Closes project, pops-up dialog with project isn't saved */
    private void closeProjectGently() {
        SaveWorkDialog saveWorkDialog = new SaveWorkDialog();
        Optional<Boolean> result = saveWorkDialog.showAndWait();
        if(result.isPresent()) {
            if(result.get())
                saveProject();
            else
                closeProject(); //Just close it
        }
    }

    /** Closes project, doesn't care if project isn't saved */
    private void closeProject() {
        designController.closeProject();
    }

    /** Saves project to project's path, if project doesn't have path dialog will be prompt */
    private void saveProject() {
        if(designController.getProject().isPathSet()) {
            try {
                ProjectWriter writer = new ProjectWriter(designController.getProject(),
                        new File(designController.getProject().getPath()));

                if (writer.hasSucceed()) {
                    Utility.positiveAlert("Project saved to: " + writer.getSavedProjectFile().getPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save project");
            fileChooser.setInitialFileName("project");

            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Spritor projects", "*.xml");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setSelectedExtensionFilter(extensionFilter);
            File file = fileChooser.showSaveDialog(Main.stage);

            if(file != null) {
                designController.getProject().setPath(file.getPath());
                saveProject();
            }
        }
    }

    /**
     * Saves project to given path
     * @param path path to save project
     */
    private void saveProject(String path) {
        saveProject(new File(path));
    }

    /**
     * Saves project to given file
     *
     * @param file file to save project
     */
    private void saveProject(File file) {
        try {
            file.getParentFile().mkdirs();
            if (file.canWrite() || !file.exists()) {
                ProjectWriter writer = new ProjectWriter(designController.getProject(), file);

                if (writer.hasSucceed()) {
                    Utility.positiveAlert("Project saved to: " + writer.getSavedProjectFile().getPath());
                }
            } else {
                Utility.errorAlert("Cannot write project to file: " + file.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    /** File -> Generate */
    private void generateButton() {
        if(designController.hasProject()) {
            GenerateDialog generateDialog = new GenerateDialog();
            Optional<Pair<Integer, File>> result = generateDialog.showAndWait();
            if (result.isPresent()) {
                Pair<Integer, File> pair = result.get();
                generate(designController.getProject(), pair.getValue(), pair.getKey());
            }
        } else
            Utility.errorAlert("There is no currently opened project.");
    }

    /**
     * Generates sprites
     *
     * @param project   to be generated
     * @param directory as output for sprites
     * @param number    amount of generated sprites
     */
    private void generate(Project project, File directory, int number) {
        if (project != null && directory.isDirectory() && number > 0) {
            if (generator == null || !generator.isWorking()) {
                generator = new Generator(directory, project);
                generator.start(number);
            }
        }
    }

    /**
     * Loads resources pane view
     *
     * @return com.poprosturonin.controller of loaded resources pane
     */
    private AssetsController loadAssetsPane() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/assetspane.fxml"));
            Parent root = fxmlLoader.load();
            assetsPane.getChildren().add(root);
            Utility.setAnchorAll(root, 0);
            return fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.exceptionAlert(e);
            return null;
        }
    }

    /**
     * Loads canvas pane view
     *
     * @return com.poprosturonin.controller of loaded canvas pane
     */
    private CanvasController loadCanvasPane() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/canvaspane.fxml"));
            Parent root = fxmlLoader.load();
            canvasPane.getChildren().add(root);
            Utility.setAnchorAll(root, 0);
            return fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.exceptionAlert(e);
            return null;
        }
    }

    /** Loads design pane view
     * @return com.poprosturonin.controller of loaded design pane
     */
    private DesignController loadDesignPane() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/designpane.fxml"));
            Parent root = fxmlLoader.load();
            designPane.getChildren().add(root);
            Utility.setAnchorAll(root, 0);
            return fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.exceptionAlert(e);
            return null;
        }
    }

    public AssetsController getAssetsPane() {
        return assetsController;
    }

    public CanvasController getCanvasPane() {
        return canvasController;
    }

    public DesignController getDesignPane() {
        return designController;
    }
}
