package sales.kiwamirembe.com.classes;

public class InventoryMovement {

    private int inventoryMovementId;
    private int inventoryMovementItemId;
    private String inventoryMovementType;
    private int inventoryMovementQuantityChanged;
    private String inventoryMovementReason;
    private long inventoryMovementTimestamp;
    private String inventoryMovementInitiator;
    private int inventoryMovementSourceLocationId; // Identifier of the source location
    private int inventoryMovementDestinationLocationId; // Identifier of the destination location
    private int inventoryMovementWebSynced;

    public InventoryMovement() {
    }

    public InventoryMovement(int inventoryMovementId, int inventoryMovementItemId, String inventoryMovementType, int inventoryMovementQuantityChanged, String inventoryMovementReason, long inventoryMovementTimestamp,
                             String inventoryMovementInitiator, int inventoryMovementSourceLocationId, int inventoryMovementDestinationLocationId, int inventoryMovementWebSynced) {
        this.inventoryMovementId = inventoryMovementId;
        this.inventoryMovementItemId = inventoryMovementItemId;
        this.inventoryMovementType = inventoryMovementType;
        this.inventoryMovementQuantityChanged = inventoryMovementQuantityChanged;
        this.inventoryMovementReason = inventoryMovementReason;
        this.inventoryMovementTimestamp = inventoryMovementTimestamp;
        this.inventoryMovementInitiator = inventoryMovementInitiator;
        this.inventoryMovementSourceLocationId = inventoryMovementSourceLocationId;
        this.inventoryMovementDestinationLocationId = inventoryMovementDestinationLocationId;
        this.inventoryMovementWebSynced = inventoryMovementWebSynced;
    }

    public InventoryMovement(int inventoryMovementItemId, String inventoryMovementType, int inventoryMovementQuantityChanged, String inventoryMovementReason, long inventoryMovementTimestamp,
                             String inventoryMovementInitiator, int inventoryMovementSourceLocationId, int inventoryMovementDestinationLocationId, int inventoryMovementWebSynced) {
        this.inventoryMovementItemId = inventoryMovementItemId;
        this.inventoryMovementType = inventoryMovementType;
        this.inventoryMovementQuantityChanged = inventoryMovementQuantityChanged;
        this.inventoryMovementReason = inventoryMovementReason;
        this.inventoryMovementTimestamp = inventoryMovementTimestamp;
        this.inventoryMovementInitiator = inventoryMovementInitiator;
        this.inventoryMovementSourceLocationId = inventoryMovementSourceLocationId;
        this.inventoryMovementDestinationLocationId = inventoryMovementDestinationLocationId;
        this.inventoryMovementWebSynced = inventoryMovementWebSynced;
    }

    public int getInventoryMovementId() {
        return inventoryMovementId;
    }

    public void setInventoryMovementId(int inventoryMovementId) {
        this.inventoryMovementId = inventoryMovementId;
    }

    public int getInventoryMovementItemId() {
        return inventoryMovementItemId;
    }

    public void setInventoryMovementItemId(int inventoryMovementItemId) {
        this.inventoryMovementItemId = inventoryMovementItemId;
    }

    public String getInventoryMovementType() {
        return inventoryMovementType;
    }

    public void setInventoryMovementType(String inventoryMovementType) {
        this.inventoryMovementType = inventoryMovementType;
    }

    public int getInventoryMovementQuantityChanged() {
        return inventoryMovementQuantityChanged;
    }

    public void setInventoryMovementQuantityChanged(int inventoryMovementQuantityChanged) {
        this.inventoryMovementQuantityChanged = inventoryMovementQuantityChanged;
    }

    public String getInventoryMovementReason() {
        return inventoryMovementReason;
    }

    public void setInventoryMovementReason(String inventoryMovementReason) {
        this.inventoryMovementReason = inventoryMovementReason;
    }

    public long getInventoryMovementTimestamp() {
        return inventoryMovementTimestamp;
    }

    public void setInventoryMovementTimestamp(long inventoryMovementTimestamp) {
        this.inventoryMovementTimestamp = inventoryMovementTimestamp;
    }

    public String getInventoryMovementInitiator() {
        return inventoryMovementInitiator;
    }

    public void setInventoryMovementInitiator(String inventoryMovementInitiator) {
        this.inventoryMovementInitiator = inventoryMovementInitiator;
    }

    public int getInventoryMovementSourceLocationId() {
        return inventoryMovementSourceLocationId;
    }

    public void setInventoryMovementSourceLocationId(int inventoryMovementSourceLocationId) {
        this.inventoryMovementSourceLocationId = inventoryMovementSourceLocationId;
    }

    public int getInventoryMovementDestinationLocationId() {
        return inventoryMovementDestinationLocationId;
    }

    public void setInventoryMovementDestinationLocationId(int inventoryMovementDestinationLocationId) {
        this.inventoryMovementDestinationLocationId = inventoryMovementDestinationLocationId;
    }

    public int getInventoryMovementWebSynced() {
        return inventoryMovementWebSynced;
    }

    public void setInventoryMovementWebSynced(int inventoryMovementWebSynced) {
        this.inventoryMovementWebSynced = inventoryMovementWebSynced;
    }

    @Override
    public String toString() {
        return "InventoryMovement{" +
                "inventoryMovementId=" + inventoryMovementId +
                ", inventoryMovementItemId=" + inventoryMovementItemId +
                ", inventoryMovementType='" + inventoryMovementType + '\'' +
                ", inventoryMovementQuantityChanged=" + inventoryMovementQuantityChanged +
                ", inventoryMovementReason='" + inventoryMovementReason + '\'' +
                ", inventoryMovementTimestamp=" + inventoryMovementTimestamp +
                ", inventoryMovementInitiator='" + inventoryMovementInitiator + '\'' +
                ", inventoryMovementSourceLocationId=" + inventoryMovementSourceLocationId +
                ", inventoryMovementDestinationLocationId=" + inventoryMovementDestinationLocationId +
                ", inventoryMovementWebSynced=" + inventoryMovementWebSynced +
                '}';
    }
}


/*

1. `movementId`: This is a unique identifier for each inventory movement record. It is used to distinguish different movements.

2. `inventoryItemId`: This represents the identifier of the inventory item associated with the movement. It helps link the movement to a specific inventory item.

3. `movementType`: This field indicates the type of movement, such as "sale", "purchase", "return", or any other relevant type. It describes whether the movement increased or decreased the inventory.

4. `quantityChanged`: This field holds the quantity by which the inventory was changed due to the movement. It can be a positive value for additions and a negative value for deductions.

5. `reason`: This field stores the reason or description for the inventory movement. It could provide additional information about why the inventory change occurred.

6. `timestamp`: This variable represents the timestamp when the inventory movement took place. It is usually recorded using a long value representing milliseconds since the epoch.

7. `initiator`: This field holds information about who initiated the inventory movement. It could be a user, system, or any entity responsible for the change.

 */
