package com.nilunder.bdx;

import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;

import com.nilunder.bdx.inputs.*;
import com.nilunder.bdx.audio.*;
import com.nilunder.bdx.utils.ArrayListNamed;
import com.nilunder.bdx.utils.Profiler;

public class Bdx{

	public static class Display{
		public void fullscreen(boolean full){
			Graphics.DisplayMode dm = Gdx.graphics.getDesktopDisplayMode();
			Gdx.graphics.setDisplayMode(dm.width, dm.height, full);
		}
		public boolean fullscreen(){
			return Gdx.graphics.isFullscreen();
		}
		public void clearColor(float r, float g, float b, float a){
			Gdx.gl.glClearColor(r, g, b, a);
		}
	}

	public static class ArrayListScenes extends ArrayListNamed<Scene>{
		public boolean add(Scene scene){
			boolean ret = super.add(scene);
			if (scene.objects == null)
				scene.init();
			return ret;
		}
		public void add(int index, Scene scene){
			super.add(index, scene);
			if (scene.objects == null)
				scene.init();
		}
		public Scene set(int index, Scene scene){
			Scene old = remove(index);
			add(index, scene);
			return old;
		}
		public ArrayList<String> available(){
			ArrayList<String> scenes = new ArrayList<String>();
			FileHandle[] files = Gdx.files.internal("bdx/scenes/").list("bdx");
			for (FileHandle file : files){
				scenes.add(file.name().replace(".bdx", ""));
			}
			return scenes;
		}

	}

	public static final float TICK_TIME = 1f/60f;
	public static float time;
	public static Profiler profiler;
	public static ArrayListScenes scenes;
	public static Display display;
	public static Sounds sounds;
	public static Music music;
	public static Mouse mouse;
	public static Keyboard keyboard;
	public static ArrayList<Finger> fingers;
	public static ArrayList<Finger> allocatedFingers;

	public static void init(){
		time = 0;
		profiler = new Profiler();
		display = new Display();
		scenes = new ArrayListScenes();
		sounds = new Sounds();
		music = new Music();
		mouse = new Mouse();
		keyboard = new Keyboard();
		fingers = new ArrayList<Finger>(); 

		allocatedFingers = new ArrayList<Finger>();
		for (int i = 0; i < 10; ++i){
			allocatedFingers.add(new Finger(i));
		}
	}

	public static void updateInput(){
		time += TICK_TIME;
		++Keyboard.t;
		fingers.clear();
		for (Finger f : allocatedFingers){
			if (f.down() || f.up())
				fingers.add(f);
		}
	}
}
