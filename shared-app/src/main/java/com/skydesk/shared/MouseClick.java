package com.skydesk.shared;

import java.io.Serializable;

public record MouseClick(String button, int x, int y) implements Serializable {

}
