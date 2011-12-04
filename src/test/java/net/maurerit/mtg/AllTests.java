package net.maurerit.mtg;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DeckImporterFactoryTest.class, MtgVaultDeckImporterTest.class, TcgPlayerDeckImporterTest.class })
public class AllTests {

}
