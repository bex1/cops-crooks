package com.dat255.project.android.copsandcrooks;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.dat255.project.android.copsandcrooks.model.AbstractTileTest;
import com.dat255.project.android.copsandcrooks.model.CopCarTest;
import com.dat255.project.android.copsandcrooks.model.CrookTest;
import com.dat255.project.android.copsandcrooks.model.GameModelTest;
import com.dat255.project.android.copsandcrooks.model.GetAwayTileTest;
import com.dat255.project.android.copsandcrooks.model.HideoutTest;
import com.dat255.project.android.copsandcrooks.model.IntelligenceAgencyTileTest;
import com.dat255.project.android.copsandcrooks.model.OfficerTest;
import com.dat255.project.android.copsandcrooks.model.PathFinderTest;
import com.dat255.project.android.copsandcrooks.model.PlayerTest;
import com.dat255.project.android.copsandcrooks.model.RobbableBuildingTileTest;
import com.dat255.project.android.copsandcrooks.model.TramLineTest;
import com.dat255.project.android.copsandcrooks.model.TravelAgencyTileTest;
import com.dat255.project.android.copsandcrooks.model.WalletTest;

@RunWith(Suite.class)
@SuiteClasses({ AbstractTileTest.class, CopCarTest.class,
		CrookTest.class, GameModelTest.class,
		GetAwayTileTest.class, HideoutTest.class,
		IntelligenceAgencyTileTest.class, OfficerTest.class,
		PathFinderTest.class, PlayerTest.class, RobbableBuildingTileTest.class,
		TramLineTest.class, TravelAgencyTileTest.class, WalletTest.class })
public class AllTests {

}
