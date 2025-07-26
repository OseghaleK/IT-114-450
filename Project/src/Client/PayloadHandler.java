package Client;

import Common.*;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Handles incoming Payloads on the client and calls ClientMain helpers.
 */
public class PayloadHandler {
    public static void handlePayload(Payload p) {
        switch (p.getType()) {
            case ASSIGN_ID: {
                int id = Integer.parseInt(p.getMessage());
                ClientMain.setMyId(id);
                System.out.println("[SYSTEM] Assigned client id: " + id);
                break;
            }
            case DIMENSION: {
                DimensionPayload dp = (DimensionPayload) p;
                ClientMain.applyDimension(dp.getWidth(), dp.getHeight());
                break;
            }
            case DRAW_SYNC: {
                CoordPayload cp = (CoordPayload) p;
                ClientMain.applyDraw(cp.getX(), cp.getY(), cp.getColor());
                break;
            }
            case POINTS: {
                ClientMain.applyPoints((PointsPayload) p);
                break;
            }
            case ROUND_START: {
                RoundStartPayload rsp = (RoundStartPayload) p;
                boolean amDrawer = (rsp.getDrawerId() == ClientMain.getMyId());
                ClientMain.setDrawer(amDrawer);
                System.out.println("[SYSTEM] Round started. Blanks: " + rsp.getBlanks());
                break;
            }
            case ROUND_END: {
                RoundEndPayload rep = (RoundEndPayload) p;
                System.out.println("[SYSTEM] Round ended. Word was: " + rep.getWord());
                break;
            }
            default:
                System.out.println("[SERVER] " + p);
        }
    }
}
