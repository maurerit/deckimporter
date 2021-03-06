package net.maurerit.mtg;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

@RunWith(AllTests.class)
@SuiteClasses({ DeckImporterFactoryTest.class,
				MtgVaultDeckImporterTest.class,
				TcgPlayerDeckImporterTest.class,
				MageFileDeckSaverTest.class,
				DeckImporterUtilsTest.class,
				MagicOnlineDeckImporterTest.class,
				MagicLeagueDeckImporterTest.class,
				TcgPlayerTournamentImporterTest.class})
public class AllTests extends Suite {
	public AllTests ( Class<?> klass, RunnerBuilder builder ) throws InitializationError {
		super(klass, builder);
		Authenticator authenticator = new Authenticator() {

	        public PasswordAuthentication getPasswordAuthentication() {
	            return (new PasswordAuthentication(System.getProperty("http.proxyUser"),
	                    System.getProperty("http.proxyPass").toCharArray()));
	        }
	    };
	    Authenticator.setDefault(authenticator);
	}
}
