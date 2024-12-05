package com.reacconmind.moderation.strategy;

import com.reacconmind.moderation.dto.ContentModerationRequest;
import com.reacconmind.moderation.dto.ContentModerationResponse;
import com.reacconmind.moderation.model.ModerationResult;

/**
 * Interfaz que define la estrategia de moderación de contenido.
 * Cada tipo de contenido (texto, imagen, etc.) tendrá su propia implementación.
 */
public interface ModerationStrategy {
    /**
     * Modera el contenido proporcionado
     * @param request La solicitud de moderación que contiene el contenido a moderar
     * @return La respuesta de moderación con el resultado del análisis
     */
    ContentModerationResponse moderate(ContentModerationRequest request);

    /**
     * Indica si esta estrategia soporta el tipo de contenido dado
     * @param contentType El tipo de contenido a moderar
     * @return true si la estrategia soporta este tipo de contenido
     */
    boolean supports(ModerationResult.ContentType contentType);
}
