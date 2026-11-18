package com.hutch.booster;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BoosterTileService extends TileService {
    private static ScheduledExecutorService scheduler;
    private static boolean isRunning = false;

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTileState();
    }

    @Override
    public void onClick() {
        super.onClick();
        isRunning = !isRunning;
        if (isRunning) {
            startSmartPing();
        } else {
            stopSmartPing();
        }
        updateTileState();
    }

    private void updateTileState() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(isRunning ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
            tile.setLabel(isRunning ? "Boost: ON" : "Hutch Boost");
            tile.updateTile();
        }
    }

    private void startSmartPing() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // Ping every 6 seconds using Google's 0-byte 204 server (No heating, No battery drain)
        scheduler.scheduleAtFixedRate(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://connectivitycheck.gstatic.com/generate_204");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("HEAD");
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.getResponseCode();
            } catch (Exception ignored) {
            } finally {
                if (conn != null) conn.disconnect();
            }
        }, 0, 6, TimeUnit.SECONDS);
    }

    private void stopSmartPing() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }
}
