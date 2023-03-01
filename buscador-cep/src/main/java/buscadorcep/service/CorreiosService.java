package buscadorcep.service;

import buscadorcep.BuscadorCepApplication;
import buscadorcep.exception.NoContentException;
import buscadorcep.exception.NotReadyException;
import buscadorcep.model.Address;
import buscadorcep.model.AddressStatus;
import buscadorcep.model.Status;
import buscadorcep.repository.AddressRepository;
import buscadorcep.repository.AddressStatusRepository;
import buscadorcep.repository.SetupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CorreiosService {
    private static Logger logger = LoggerFactory.getLogger(CorreiosService.class);
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressStatusRepository addressStatusRepository;

    @Autowired
    private SetupRepository setupRepository;
    public Status getStatus() {
        return addressStatusRepository.findById(AddressStatus.DEFAULT_ID)
                // Se ele não encontrar o o ID padrão significa que o
                .orElse(AddressStatus.builder().status(Status.NEED_SETUP).build()).getStatus();
    }

    public Address getAddressByZipcode(String zipcode) throws NoContentException, NotReadyException{
        if (!this.getStatus().equals(Status.READY)) {  //Se a leitura do status não for igual a READY ele levanta essa exception
            throw new NotReadyException();
        }

        return addressRepository.findById(zipcode).orElseThrow(NoContentException::new);
    }

    private void saveStatus(Status status){
        this.addressStatusRepository.save(AddressStatus.builder()
                .id(AddressStatus.DEFAULT_ID).status(status).build());
    }
@EventListener(ApplicationStartedEvent.class)
    protected void setupOnStartup(){
        try {
            this.setup();
        } catch (Exception e){
            BuscadorCepApplication.close(999);
        }
    }

    public void setup() throws Exception{
        logger.info("---");
        logger.info("---");
        logger.info("--- STARTING SETUP");
        logger.info("--- Please wait... This may take a few minutes");
        logger.info("---");
        logger.info("---");

            if (this.getStatus().equals(Status.NEED_SETUP)){
                this.saveStatus(Status.SETUP_RUNNING);

                try {
                    this.addressRepository.saveAll(
                            this.setupRepository.getFromOrigin());

                    logger.info("---");
                    logger.info("---");
                    logger.info("--- READY TO USE");
                    logger.info("--- Good luck my friend :)");
                    logger.info("---");
                    logger.info("---");
                } catch (Exception e){
                    this.saveStatus(Status.NEED_SETUP);
                    logger.error("Error to download/save addresses, closing the application....");
                    BuscadorCepApplication.close(999);
                }
                this.saveStatus(Status.READY);
            }
    }
}
