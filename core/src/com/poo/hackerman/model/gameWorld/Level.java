package com.poo.hackerman.model.gameWorld;

import com.poo.hackerman.model.Managers.EntityManager;
import com.poo.hackerman.model.entity.Direction;
import com.poo.hackerman.model.entity.Entity;
import com.poo.hackerman.model.entity.Position;
import com.poo.hackerman.model.entity.dynamicEntity.character.PlayerCharacter;
import com.poo.hackerman.model.entity.dynamicEntity.character.enemyCharacter.CameraGuard;
import com.poo.hackerman.model.entity.dynamicEntity.character.enemyCharacter.EnemyCharacter;
import com.poo.hackerman.model.entity.dynamicEntity.character.enemyCharacter.Guard;
import com.poo.hackerman.model.entity.staticEntity.Obstacle;
import com.poo.hackerman.model.entity.staticEntity.interactiveStaticEntity.Computer;
import com.poo.hackerman.model.entity.staticEntity.interactiveStaticEntity.Door;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by Sebastian on 7/6/17.
 */
public class Level {

    private EntityManager entityManager;

    public Level(String filename){
        this.entityManager = setEntityManager(filename);
    }

    private EntityManager setEntityManager(String filename) { //el ultimo elemento de cada array es basura para ese elemento
        //ej: guards[guards.lenght-1] no es un guard
        String s = readFile(filename);
        String[] guards = s.split(" GUARDS\n");
        String[] cameraguards = guards[guards.length - 1].split(" CAMERAGUARDS\n");
        String[] computers = cameraguards[cameraguards.length - 1].split(" COMPUTERS\n");
        String[] doors = computers[computers.length - 1].split(" DOORS\n");
        String[] desks = doors[doors.length - 1].split(" DESKS\n");
        String[] hackers = desks[desks.length - 1].split(" HACKER\n");
        String[] maps = hackers[hackers.length - 1].split(" MAP");
        String map = maps[0];
        String[] mapRows = map.split("/\n");
        LinkedList<Obstacle> obstacleList = new LinkedList<>();
        LinkedList<Computer> computerList = new LinkedList<>();
        LinkedList<EnemyCharacter> enemyList = new LinkedList<>();
        PlayerCharacter hacker = null;
        Door door = null;
        int guardIndex = 0, cameraIndex = 0, computerIndex = 0, doorIndex = 0, deskIndex = 0;
        int rowNumber = 0;
        for (String row : mapRows) {
            String[] cells = row.split(",");
            int cellNumber = 0;
            for (String cell : cells) {
                Position position = new Position(rowNumber, cellNumber);
                switch (cell){
                    case "WALL":
                        Direction direction = new Direction(0);
                        obstacleList.add(new Obstacle(position, direction, Obstacle.obstacleType.WALL));
                        break;
                    case "DOOR":
                        String[] properties = doors[doorIndex++].split(",");
                        direction = new Direction(Integer.valueOf(properties[0]));
                        door = new Door(position, direction);
                        break;
                    case "GUARD":
                        properties = guards[guardIndex++].split(",");
                        direction = new Direction(Integer.valueOf(properties[0]));
                        int velocity = Integer.valueOf(properties[1]);
                        int range = Integer.valueOf(properties[2]);
                        if(properties.length>3){
                            ArrayList<Position> instructions = new ArrayList<>();
                            for(int i=3;i<properties.length;i+=2){
                                instructions.add(new Position(i,i+1));
                            }
                            enemyList.add(new Guard(position, direction, velocity, range,instructions));
                        }
                        else {
                            enemyList.add(new Guard(position, direction, velocity, range));
                        }
                        break;
                    case "CAMERAGUARD":
                        properties = cameraguards[cameraIndex++].split(",");
                        direction = new Direction(Integer.valueOf(properties[0]));
                        range = Integer.valueOf(properties[1]);
                        if(properties.length>2){
                            ArrayList<Direction> instructions = new ArrayList<>();
                            for(int i=2;i<properties.length;i++){
                                instructions.add(new Direction(i));
                            }
                            enemyList.add(new CameraGuard(position, direction, range,instructions));
                        }
                        else {
                            enemyList.add(new CameraGuard(position, direction, range));
                        }
                        break;
                    case "HACKER":
                        properties = hackers[0].split(",");
                        direction = new Direction(Integer.valueOf(properties[0]));
                        velocity = Integer.valueOf(properties[1]);
                        hacker = new PlayerCharacter(position, direction, velocity);
                        break;
                    case "COMPUTER":
                        properties = computers[computerIndex].split(",");
                        direction = new Direction(Integer.valueOf(properties[0]));
                        int consecutiveHacks = Integer.valueOf(properties[1]);
                        computerList.add(new Computer(position, direction, consecutiveHacks));
                        break;
                    case "DESK":
                        properties = desks[deskIndex].split(",");
                        direction = new Direction(Integer.valueOf(properties[0]));
                        obstacleList.add(new Obstacle(position, direction, Obstacle.obstacleType.DESK));
                        break;
                }
                cellNumber++;
            }
            rowNumber++;
        }
        return new EntityManager(hacker,door,enemyList,computerList,obstacleList);
    }

    private String readFile(String filename) {
        File f = new File(filename);
        try {
            byte[] bytes = Files.readAllBytes(f.toPath());
            return new String(bytes,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    /*
    private EntityManager setEntityManager(LinkedList<LinkedList<Entity>> level) {

        LinkedList<Obstacle> obstacles = new LinkedList<>();
        LinkedList<Computer> computers = new LinkedList<>();
        LinkedList<EnemyCharacter> enemies = new LinkedList<>();
        PlayerCharacter hacker = null;
        Door door = null;
        for(LinkedList<Entity> row:level){
            for(Entity entity:row){
                if(entity instanceof Computer)
                    computers.add((Computer)entity);
                else if(entity instanceof Obstacle)
                    obstacles.add((Obstacle)entity);
                else if(entity instanceof EnemyCharacter)
                    enemies.add((EnemyCharacter)entity);
                else if(entity instanceof Door)
                    door=(Door) entity;
                else if(entity instanceof PlayerCharacter)
                    hacker=(PlayerCharacter)entity;
            }
        }
        return new EntityManager(hacker,door,enemies,computers,obstacles);
    }

    public LinkedList<LinkedList<Entity>> getGrid() { return grid; }
    */

}