package it.univaq.findervelox.utility;

public interface OnRequestListener {

    void onRequestCompleted(byte[] data);

    void onRequestUpdate(int progress);

}
