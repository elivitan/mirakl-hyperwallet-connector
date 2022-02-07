package com.paypal.notifications.service.hyperwallet.impl;

import com.hyperwallet.clientsdk.Hyperwallet;
import com.hyperwallet.clientsdk.util.HyperwalletEncryption;
import com.paypal.notifications.infrastructure.configuration.NotificationsHyperwalletApiConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationsHyperwalletSDKEncryptedServiceImplTest {

	private static final String SERVER = "server";

	private static final String PASSWORD = "password";

	private static final String USER_NAME = "userName";

	private static final String PROGRAM_TOKEN = "programToken";

	@InjectMocks
	private NotificationsHyperwalletSDKEncryptedServiceImpl testObj;

	@Mock
	private HyperwalletEncryption hyperwalletEncryptionMock;

	@Mock
	private NotificationsHyperwalletApiConfig notificationsHyperwalletApiConfigMock;

	@Test
	void getHyperwalletInstance_shouldReturnAnHyperwalletInstanceWithEncryptedOption() {
		when(notificationsHyperwalletApiConfigMock.getUsername()).thenReturn(USER_NAME);
		when(notificationsHyperwalletApiConfigMock.getPassword()).thenReturn(PASSWORD);
		when(notificationsHyperwalletApiConfigMock.getServer()).thenReturn(SERVER);

		final Hyperwallet result = testObj.getHyperwalletInstance(PROGRAM_TOKEN);

		assertThat(result).hasFieldOrPropertyWithValue("apiClient.hyperwalletEncryption", hyperwalletEncryptionMock);
	}

}
