package main;

import entity.Entity;

public class CollisionChecker {

	private GamePanel gp;

	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}

	public void checkTile(Entity entity) {
		int entityLeftWorldX = entity.getX() + entity.getSolidArea().x;
		int entityRightWorldX = entity.getX() + entity.getSolidArea().x + entity.getSolidArea().width;
		int entityTopWorldY = entity.getY() + entity.getSolidArea().y;
		int entityBottomWorldY = entity.getY() + entity.getSolidArea().y + entity.getSolidArea().height;

		int entityLeftCol = entityLeftWorldX / gp.getTileSize();
		int entityRightCol = entityRightWorldX / gp.getTileSize();
		int entityTopRow = entityTopWorldY / gp.getTileSize();
		int entityBottomRow = entityBottomWorldY / gp.getTileSize();

		int tileNum1, tileNum2;

		switch (entity.getDirection()) {
		case "up":
			entityTopRow = (entityTopWorldY - entity.getSpeed()) / gp.getTileSize();
			if (entityLeftCol >= 0 && entityTopRow >= 0 && entityLeftCol < gp.getTileM().getMapTileNum().length
					&& entityTopRow < gp.getTileM().getMapTileNum()[0].length) {
				tileNum1 = gp.getTileM().getMapTileNum()[entityLeftCol][entityTopRow];
			} else {
				// Handle the out-of-bounds case or return early
				return;
			}

			if (entityRightCol >= 0 && entityTopRow >= 0 && entityRightCol < gp.getTileM().getMapTileNum().length
					&& entityTopRow < gp.getTileM().getMapTileNum()[0].length) {
				tileNum2 = gp.getTileM().getMapTileNum()[entityRightCol][entityTopRow];
			} else {
				// Handle the out-of-bounds case or return early
				return;
			}

			if (tileNum1 < gp.getTileM().getTile().length && gp.getTileM().getTile()[tileNum1].collision
					|| tileNum2 < gp.getTileM().getTile().length && gp.getTileM().getTile()[tileNum2].collision) {
				entity.setCollisionOn(true);
			}
			break;

		case "down":
			entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / gp.getTileSize();
			if (entityLeftCol >= 0 && entityBottomRow >= 0 && entityLeftCol < gp.getTileM().getMapTileNum().length
					&& entityBottomRow < gp.getTileM().getMapTileNum()[0].length) {
				tileNum1 = gp.getTileM().getMapTileNum()[entityLeftCol][entityBottomRow];
			} else {
				// Handle the out-of-bounds case or return early
				return;
			}

			if (entityRightCol >= 0 && entityBottomRow >= 0 && entityRightCol < gp.getTileM().getMapTileNum().length
					&& entityBottomRow < gp.getTileM().getMapTileNum()[0].length) {
				tileNum2 = gp.getTileM().getMapTileNum()[entityRightCol][entityBottomRow];
			} else {
				// Handle the out-of-bounds case or return early
				return;
			}

			if (tileNum1 < gp.getTileM().getTile().length && gp.getTileM().getTile()[tileNum1].collision
					|| tileNum2 < gp.getTileM().getTile().length && gp.getTileM().getTile()[tileNum2].collision) {
				entity.setCollisionOn(true);
			}
			break;

		case "left":
			entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / gp.getTileSize();
			if (entityLeftCol >= 0 && entityTopRow >= 0 && entityLeftCol < gp.getTileM().getMapTileNum().length
					&& entityTopRow < gp.getTileM().getMapTileNum()[0].length) {
				tileNum1 = gp.getTileM().getMapTileNum()[entityLeftCol][entityTopRow];
			} else {
				// Handle the out-of-bounds case or return early
				return;
			}

			if (entityLeftCol >= 0 && entityBottomRow >= 0 && entityLeftCol < gp.getTileM().getMapTileNum().length
					&& entityBottomRow < gp.getTileM().getMapTileNum()[0].length) {
				tileNum2 = gp.getTileM().getMapTileNum()[entityLeftCol][entityBottomRow];
			} else {
				// Handle the out-of-bounds case or return early
				return;
			}

			if (tileNum1 < gp.getTileM().getTile().length && gp.getTileM().getTile()[tileNum1].collision
					|| tileNum2 < gp.getTileM().getTile().length && gp.getTileM().getTile()[tileNum2].collision) {
				entity.setCollisionOn(true);
			}
			break;

		case "right":
			entityRightCol = (entityRightWorldX + entity.getSpeed()) / gp.getTileSize();
			if (entityRightCol >= 0 && entityTopRow >= 0 && entityRightCol < gp.getTileM().getMapTileNum().length
					&& entityTopRow < gp.getTileM().getMapTileNum()[0].length) {
				tileNum1 = gp.getTileM().getMapTileNum()[entityRightCol][entityTopRow];
			} else {
				// Handle the out-of-bounds case or return early
				return;
			}

			if (entityRightCol >= 0 && entityBottomRow >= 0 && entityRightCol < gp.getTileM().getMapTileNum().length
					&& entityBottomRow < gp.getTileM().getMapTileNum()[0].length) {
				tileNum2 = gp.getTileM().getMapTileNum()[entityRightCol][entityBottomRow];
			} else {
				// Handle the out-of-bounds case or return early
				return;
			}

			if (tileNum1 < gp.getTileM().getTile().length && gp.getTileM().getTile()[tileNum1].collision
					|| tileNum2 < gp.getTileM().getTile().length && gp.getTileM().getTile()[tileNum2].collision) {
				entity.setCollisionOn(true);
			}
			break;
		}
	}
}
