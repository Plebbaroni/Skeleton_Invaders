package main;

import entity.Entity;

public class CollisionChecker {
	
	GamePanel gp;
	
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}
	
	public void checkTile(Entity entity) {
		int entityLeftWorldX = entity.getX() + entity.getSolidArea().x;
		int entityRightWorldX = entity.getX() + entity.getSolidArea().x + entity.getSolidArea().width;
		int entityTopWorldY = entity.getY() + entity.getSolidArea().y;
		int entityBottomWorldY = entity.getY() + entity.getSolidArea().y + entity.getSolidArea().height;
		
		int entityLeftCol = entityLeftWorldX/gp.tileSize;
		int entityRightCol = entityRightWorldX/gp.tileSize;
		int entityTopRow = entityTopWorldY/gp.tileSize;
		int entityBottomRow = entityBottomWorldY/gp.tileSize;
		
		int tileNum1, tileNum2;
		
		switch(entity.getDirection()) {
		 case "up":
	            entityTopRow = (entityTopWorldY - entity.getSpeed()) / gp.tileSize;
	            if (entityLeftCol >= 0 && entityTopRow >= 0 &&
	                entityLeftCol < gp.tileM.mapTileNum.length && entityTopRow < gp.tileM.mapTileNum[0].length) {
	                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
	            } else {
	                // Handle the out-of-bounds case or return early
	                return;
	            }

	            if (entityRightCol >= 0 && entityTopRow >= 0 &&
	                entityRightCol < gp.tileM.mapTileNum.length && entityTopRow < gp.tileM.mapTileNum[0].length) {
	                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
	            } else {
	                // Handle the out-of-bounds case or return early
	                return;
	            }

	            if (tileNum1 < gp.tileM.tile.length && gp.tileM.tile[tileNum1].collision ||
	                tileNum2 < gp.tileM.tile.length && gp.tileM.tile[tileNum2].collision) {
	                entity.setCollisionOn(true);
	            }
	            break;

	        case "down":
	            entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / gp.tileSize;
	            if (entityLeftCol >= 0 && entityBottomRow >= 0 &&
	                entityLeftCol < gp.tileM.mapTileNum.length && entityBottomRow < gp.tileM.mapTileNum[0].length) {
	                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
	            } else {
	                // Handle the out-of-bounds case or return early
	                return;
	            }

	            if (entityRightCol >= 0 && entityBottomRow >= 0 &&
	                entityRightCol < gp.tileM.mapTileNum.length && entityBottomRow < gp.tileM.mapTileNum[0].length) {
	                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
	            } else {
	                // Handle the out-of-bounds case or return early
	                return;
	            }

	            if (tileNum1 < gp.tileM.tile.length && gp.tileM.tile[tileNum1].collision ||
	                tileNum2 < gp.tileM.tile.length && gp.tileM.tile[tileNum2].collision) {
	                entity.setCollisionOn(true);
	            }
	            break;

	        case "left":
	            entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / gp.tileSize;
	            if (entityLeftCol >= 0 && entityTopRow >= 0 &&
	                entityLeftCol < gp.tileM.mapTileNum.length && entityTopRow < gp.tileM.mapTileNum[0].length) {
	                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
	            } else {
	                // Handle the out-of-bounds case or return early
	                return;
	            }

	            if (entityLeftCol >= 0 && entityBottomRow >= 0 &&
	                entityLeftCol < gp.tileM.mapTileNum.length && entityBottomRow < gp.tileM.mapTileNum[0].length) {
	                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
	            } else {
	                // Handle the out-of-bounds case or return early
	                return;
	            }

	            if (tileNum1 < gp.tileM.tile.length && gp.tileM.tile[tileNum1].collision ||
	                tileNum2 < gp.tileM.tile.length && gp.tileM.tile[tileNum2].collision) {
	                entity.setCollisionOn(true);
	            }
	            break;

	        case "right":
	            entityRightCol = (entityRightWorldX + entity.getSpeed()) / gp.tileSize;
	            if (entityRightCol >= 0 && entityTopRow >= 0 &&
	                entityRightCol < gp.tileM.mapTileNum.length && entityTopRow < gp.tileM.mapTileNum[0].length) {
	                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
	            } else {
	                // Handle the out-of-bounds case or return early
	                return;
	            }

	            if (entityRightCol >= 0 && entityBottomRow >= 0 &&
	                entityRightCol < gp.tileM.mapTileNum.length && entityBottomRow < gp.tileM.mapTileNum[0].length) {
	                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
	            } else {
	                // Handle the out-of-bounds case or return early
	                return;
	            }

	            if (tileNum1 < gp.tileM.tile.length && gp.tileM.tile[tileNum1].collision ||
	                tileNum2 < gp.tileM.tile.length && gp.tileM.tile[tileNum2].collision) {
	                entity.setCollisionOn(true);
	            }
	            break;
		}
	}
}
