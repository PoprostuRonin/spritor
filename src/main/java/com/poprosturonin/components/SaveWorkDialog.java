/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.components;

import com.poprosturonin.controller.Utility;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * Save work dialog. Gives 3 options to user:
 * - save work
 * - don't save and continue
 * - cancel operation
 * As result returns true for save,
 * false for donn't save and null/empty for cancel.
 */
public class SaveWorkDialog extends Dialog<Boolean> {
    public SaveWorkDialog() {
        getDialogPane().setId("saveWorkDialog");

        setTitle("Spritor");
        setContentText("Do want to save changes to this project?");
        Utility.setDefaultIcon(this);

        ButtonType yes = new ButtonType("Save", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Don't save", ButtonBar.ButtonData.NO);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        getDialogPane().getButtonTypes().add(yes);
        getDialogPane().getButtonTypes().add(no);
        getDialogPane().getButtonTypes().add(cancel);

        setResultConverter(dialogButton -> {
            if (dialogButton == yes) {
                return true; //Save
            } else if (dialogButton == no) {
                return false; //Don't save
            } else { //Cancel or exit
                return null; //Cancel
            }
        });
    }
}
