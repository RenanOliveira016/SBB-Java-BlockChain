import joblib
from flask import Flask, request, jsonify
import pandas as pd

# --- 1. CARREGAR O MODELO TREINADO ---

# O nome do arquivo que salvamos anteriormente
MODEL_FILENAME = 'fraud_model.joblib' 
try:
    # Carrega o modelo treinado (o cérebro de ML)
    model = joblib.load(MODEL_FILENAME)
    print(f"Modelo '{MODEL_FILENAME}' carregado com sucesso.")
except FileNotFoundError:
    print(f"ERRO: Arquivo de modelo '{MODEL_FILENAME}' não encontrado. Execute train_model.py primeiro.")
    exit()

# --- 2. INICIALIZAR A APLICAÇÃO FLASK ---

app = Flask(__name__)

# O endpoint que a aplicação Java irá chamar.
# Ele aceita requisições HTTP POST.
@app.route('/predict_fraud', methods=['POST'])
def predict():
    # 1. RECEBER OS DADOS DE ENTRADA (JSON)
    # Exemplo de JSON que o Java deve enviar: 
    # {"amount": 50.0, "time_since_last_tx": 100.5, "sender_risk_score": 0.8, "recipient_is_new": 1}
    
    try:
        data = request.get_json(force=True)
    except Exception as e:
        return jsonify({"error": "Formato JSON inválido"}), 400

    # 2. VALIDAR E PREPARAR OS DADOS PARA O MODELO
    
    # As features precisam estar na ordem exata do treinamento!
    features = ['amount', 'time_since_last_tx', 'sender_risk_score', 'recipient_is_new']
    
    # Cria um DataFrame (a "linha de dados") com os dados recebidos
    try:
        input_data = pd.DataFrame([data], columns=features)
    except Exception as e:
        return jsonify({"error": f"Erro na estrutura dos dados: {e}"}), 400

    # 3. FAZER A PREVISÃO
    
    # O predict_proba retorna a probabilidade de 0 (legítimo) e 1 (fraude)
    # Queremos a probabilidade de fraude (índice 1)
    probabilities = model.predict_proba(input_data)[0]
    risk_score = probabilities[1] # Probabilidade de ser fraude (1)

    # 4. RETORNAR O RESULTADO
    
    # Retorna o resultado em um formato JSON limpo para o Java
    response = {
        "status": "success",
        "fraud_risk_score": float(risk_score) # Converte para float padrão para JSON
    }
    
    return jsonify(response)


# --- 3. EXECUTAR O SERVIDOR ---

if __name__ == '__main__':
    print("Servidor Flask pronto para receber previsões...")
    # Roda o servidor na porta 5000 (padrão)
    app.run(port=5000, debug=True)