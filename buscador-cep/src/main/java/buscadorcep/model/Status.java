package buscadorcep.model;

public enum Status {
    NEED_SETUP, // necessario baixar csv dos correios
    SETUP_RUNNING, // baixando/salvando no banco de dados
    READY; // Serviço pode ser utilizado
}
