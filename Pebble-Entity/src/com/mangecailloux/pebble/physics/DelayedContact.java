package com.mangecailloux.pebble.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

public class DelayedContact 
{
	public Fixture fixtureA;
	public Fixture fixtureB;
	public int	   childIndexA;
	public int	   childIndexB;
	
	public DelayedContact()
	{
		
	}
	
	public DelayedContact(Contact contact)
	{
		setFromContact(contact);
	}
	
	public void setFromContact(Contact contact)
	{
		fixtureA = contact.getFixtureA();
		fixtureB = contact.getFixtureB();
		childIndexA = contact.getChildIndexA();
		childIndexB = contact.getChildIndexB();
	}
}
