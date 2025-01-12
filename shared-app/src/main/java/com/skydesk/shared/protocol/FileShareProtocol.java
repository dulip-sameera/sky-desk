package com.skydesk.shared.protocol;

import java.io.Serializable;

public record FileShareProtocol(String fileName, Long fileSize, byte[] fileContent) implements Serializable {
}
