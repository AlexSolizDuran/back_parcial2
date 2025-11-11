package com.trendora.tienda.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Sube un archivo a Cloudinary y devuelve la URL pública.
     */
    public String uploadImagen(MultipartFile multipartFile) throws IOException {
        // ... (Tu método uploadImagen que ya tienes) ...
        // 1. Convertir MultipartFile a un archivo temporal
        File file = convertMultiPartToFile(multipartFile);

        try {
            // 2. Subir el archivo a Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

            // 3. Obtener la URL segura (https) del resultado
            String secureUrl = (String) uploadResult.get("secure_url");
            
            return secureUrl;

        } finally {
            // 4. Borrar el archivo temporal (¡Muy importante!)
            file.delete();
        }
    }

    // --- 1. AÑADE ESTE NUEVO MÉTODO PARA BORRAR ---
    /**
     * Elimina una imagen de Cloudinary usando su URL pública.
     */
    public void eliminarImagen(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return; // No hay nada que borrar
        }

        try {
            // Extraemos el Public ID de la URL
            String publicId = extraerPublicIdDesdeUrl(imageUrl);
            
            // Usamos la API 'destroy' de Cloudinary
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            
            System.out.println("Imagen eliminada de Cloudinary: " + publicId);

        } catch (Exception e) {
            // Maneja la excepción (p.ej. si la URL es inválida o el borrado falla)
            System.err.println("Error al eliminar imagen de Cloudinary: " + e.getMessage());
            // Opcionalmente, puedes relanzar la excepción si prefieres que la transacción falle
            // throw new IOException("Error al eliminar imagen de Cloudinary", e);
        }
    }

    // --- 2. AÑADE ESTE MÉTODO DE AYUDA ---
    /**
     * Extrae el 'public_id' de una URL de Cloudinary.
     * Ej: de "https://.../v12345/carpeta/imagen.jpg" extrae "carpeta/imagen"
     */
    private String extraerPublicIdDesdeUrl(String imageUrl) {
        // Expresión regular para encontrar el public_id
        // Busca la parte después de /v[0-9]+ / y antes de la extensión de archivo
        Pattern pattern = Pattern.compile("/v[0-9]+/([^.]+)");
        Matcher matcher = pattern.matcher(imageUrl);

        if (matcher.find()) {
            return matcher.group(1); // Retorna el grupo capturado (el public_id)
        }
        
        // Fallback por si la URL no tiene versión (v12345)
        // Busca lo último después de "upload/" y antes de la extensión
        try {
            String[] parts = imageUrl.split("/upload/");
            if (parts.length > 1) {
                String segment = parts[1];
                return segment.substring(0, segment.lastIndexOf('.'));
            }
        } catch (Exception e) {
            System.err.println("No se pudo parsear el public_id (fallback): " + imageUrl);
        }

        throw new IllegalArgumentException("No se pudo extraer el public_id de la URL: " + imageUrl);
    }

    /**
     * Método de ayuda para convertir un MultipartFile en un File.
     */
    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
         // ... (Tu método convertMultiPartToFile que ya tienes) ...
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "temp_file";
        }
        
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + originalFilename);
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }
}