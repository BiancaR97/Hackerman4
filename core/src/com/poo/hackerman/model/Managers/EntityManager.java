package com.poo.hackerman.model.Managers;

import com.poo.hackerman.model.entity.Entity;
import com.poo.hackerman.model.entity.dynamicEntity.character.PlayerCharacter;
import com.poo.hackerman.model.entity.dynamicEntity.character.enemyCharacter.EnemyCharacter;
import com.poo.hackerman.model.entity.staticEntity.Obstacle;
import com.poo.hackerman.model.entity.staticEntity.interactiveStaticEntity.Computer;
import com.poo.hackerman.model.entity.staticEntity.interactiveStaticEntity.Door;
import com.poo.hackerman.model.gameWorld.Grid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franciscosanguineti on 31/5/17.
 */
public class EntityManager {

    private PlayerCharacter player;
    private Door door;
    private List<EnemyCharacter> enemies;
    private List<Computer> computers;
    private List<Obstacle> obstacles;

    public EntityManager(PlayerCharacter player, Door door, List<EnemyCharacter> enemies, List<Computer> computers, List<Obstacle> obstacles) {
        this.player = player;
        this.door = door;
        this.enemies = enemies;
        this.computers = computers;
        this.obstacles = obstacles;
    }

    public void tick() {
        player.tick();
        for(EnemyCharacter enemy: enemies) {
            enemy.tick();
        }
    }

    /**
     * @return True if the player was caught by an enemy
     */

    public boolean playerCaught() {
        for(EnemyCharacter enemy: enemies) {
            if(enemy.hackerDetected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param grid The grid that is set to each of the entities
     */

    public void setGrid(Grid grid) {
        if(enemies != null) {
            for (EnemyCharacter enemy : enemies) {
                enemy.setGrid(grid);
            }
        }
        player.setGrid(grid);
    }

    /**
     * @return A list that contains all the entities
     */

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<Entity>();
        if(player!=null) {
            entities.add(player);
        }
        if(door != null) {
            entities.add(door);
        }
        if(enemies != null) {
            entities.addAll(enemies);
        }
        if(computers != null) {
            entities.addAll(computers);
        }
        if(obstacles != null) {
            entities.addAll(obstacles);
        }
        return entities;
    }

    public PlayerCharacter getPlayer() {
        return player;
    }

    public Door getDoor() {
        return door;
    }

    public List<EnemyCharacter> getEnemies() {
        return enemies;
    }

    public List<Computer> getComputers() {
        return computers;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void addEnemy(EnemyCharacter enemy) {
        enemies.add(enemy);
    }

    public void addComputer(Computer computer) {
        computers.add(computer);
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

}

