package com.sofkau.stepdefinitions;

import com.sofkau.setup.ApiSetUp;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import java.nio.charset.StandardCharsets;

import static com.sofkau.models.HeadersGrados.headersGrados;
import static com.sofkau.questions.ResponseSoap.responseSoap;
import static com.sofkau.tasks.DoPostSoap.doPostSoap;
import static com.sofkau.utils.ManageFile.readFile;
import static com.sofkau.utils.Path.*;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.CoreMatchers.containsString;

public class TemperaturaStepDefinitions extends ApiSetUp {
    private static final Logger LOGGER = Logger.getLogger(TemperaturaStepDefinitions.class);
    private String body;

    @Given("a user that wants to convert celcius {int} to fahrenheit")
    public void aUserThatWantsToConvertCelciusToFahrenheit(Integer int1) {
        try {
            setUp(SOAP_TEMPERATURE_BASE_URL.getValue());
            LOGGER.info("Se inicia la automatizacion");
            loadBody(int1);
        } catch (Exception e) {
            LOGGER.info("Fallo la configuracion inicial");
            LOGGER.warn(e.getMessage());
            Assertions.fail();
        }
    }

    @When("the user sends the request to the converion api")
    public void theUserSendsTheRequestToTheConverionApi() {
        try{
            LOGGER.info(SOAP_TEMPERATURE_BASE_URL.getValue()+RESOURCE_CELCIUS.getValue());
            actor.attemptsTo(
                    doPostSoap().withTheHeaders(headersGrados().getHeadersCollection())
                            .andTheBody(body)
                            .andTheResource(RESOURCE_CELCIUS.getValue())
            );
            LOGGER.info("Se realiza la peticion");
        }catch (Exception e){
            LOGGER.info("Fallo la peticion");
            LOGGER.warn(e.getMessage());
            Assertions.fail();
        }
    }

    @Then("the user gets the convertion result {string}")
    public void theUserGetsTheConvertionResult(String string) {
        try{
            LOGGER.info(new String(LastResponse.received().answeredBy(actor).asByteArray(), StandardCharsets.UTF_8));
            actor.should(
                    seeThatResponse("el codigo de respuesta es: " + HttpStatus.SC_OK,
                            response -> response.statusCode(HttpStatus.SC_OK)),
                    seeThat("la temperatura es",
                            responseSoap(), containsString(string))
            );
        }catch (Exception e){
            LOGGER.info("Error al comparar");
            LOGGER.warn(e.getMessage());
            Assertions.fail();
        }
    }

    private void loadBody(Integer int1) {
        body = readFile(TEMPERATURE_BODY_PATH.getValue());
        body = String.format(body, int1+"");
    }
}
