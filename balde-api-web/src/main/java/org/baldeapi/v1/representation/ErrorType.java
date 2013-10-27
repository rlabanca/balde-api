package org.baldeapi.v1.representation;

public enum ErrorType {

	//1000 a 1999 last minute
	LAST_MINUTE_ERROR(1000, "Um erro ocorreu ao processar a requisição"),
	PARAMETER_MISSING(1001, "Parâmetro obrigatório não informado"),
	INVALID_PARAMETER(1002, "Valor de parâmetro inválido"),
	EMPTY_SHOPPINGCART(1003, "O carrinho compras está vazio"),
	SHOPPINGCART_NOT_FOUND(1004, "O carrinho de compras não foi encontrado"),
	OFFER_NOT_FOUND(1005, "Oferta não existe"),
	OFFER_NOT_AVALIABLE(1006, "Não há mais disponibilidade para este tipo de quarto"),
	INVALID_ESTABLISHMENT(1007, "Tipo de estabelecimento ou provedor de serviço inválidos"),
	INVALID_DATE(1008, "Data de checkin ou checkout com formato inválido"),
	NOT_FOUND(1009, "Não encontrado"),
	USER_ALREADY_EXISTS(1010, "Usuário já existe"),
	
	//2000 a 2999 offer provider
	OFFER_PROVIDER_ERROR(2000, "Erro do provedor de ofertas"),
	INVALID_RESERVATION(2001, "A reserva não foi feita"),
	CANCELATION_NOT_ALLOWED(2002, "Favor entrar em contato. Não foi possível efetuar o cancelamento da reserva"),
	
	//3000 a 3999 bcash
	BCASH_ERROR(3000, "Erro do bcash"),
	INVALID_OWNERSHIP(3001, "Usuário não tem acesso a este ticket"),
	INVALID_STATE_CHANGE(3002, "Só é possível informar forma de pagamento pela API"),
	INVALID_VALUE(3003, "Não foi possível confirmar o pagamento. Valor da transação financeira menor que o valor total do ticket."),
	INVALID_PAYMENT(3004, "O pagamento não foi confirmado"),
	
	//4000 a 4999 login unico
	SINGLE_SIGN_ON_ERROR(4000, "Erro ao autenticar o usuário"),
	PERMISSION_DENIED(4001, "Permissão negada");
	
	private int code;
	private String defaultMessage;
	
	private ErrorType(int code, String defaultMessage) {
		this.code = code;
		this.defaultMessage = defaultMessage;
	}

	public int getCode() {
		return code;
	}
	
	public String getDefaultMessage() {
		return defaultMessage;
	}
}
