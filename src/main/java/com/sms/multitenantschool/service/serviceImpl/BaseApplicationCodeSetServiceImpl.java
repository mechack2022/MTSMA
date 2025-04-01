package com.sms.multitenantschool.service.serviceImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.multitenantschool.model.dto.ApplicationCodesetDTO;
import com.sms.multitenantschool.model.entity.BaseApplicationCodeset;
import com.sms.multitenantschool.repository.BaseApplicationCodesetRepository;
import com.sms.multitenantschool.service.ApplicationCodesetService;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaseApplicationCodeSetServiceImpl implements ApplicationCodesetService {

    private final ObjectMapper objectMapper;
    private final BaseApplicationCodesetRepository applicationCodesetRepository;

    public BaseApplicationCodeSetServiceImpl(ObjectMapper objectMapper, BaseApplicationCodesetRepository applicationCodesetRepository) {
        this.objectMapper = objectMapper;
        this.applicationCodesetRepository = applicationCodesetRepository;
    }

    @Transactional
    @Override
    public List<ApplicationCodesetDTO> readFileData(MultipartFile file, String fileType) throws IOException {
        List<ApplicationCodesetDTO> listOfCodesets = new ArrayList<>();

        if (fileType.equalsIgnoreCase("csv")) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                System.out.println("Starting to parse CSV file");

                // Parse CSV with explicit delimiter and quote handling
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withDelimiter(',')
                        .withQuote('"')
                        .withFirstRecordAsHeader()
                        .withIgnoreSurroundingSpaces()
                        .withTrim());

                // Validate headers
                Set<String> expectedHeaders = Set.of("display", "description", "codeset_group", "code", "archived", "version");
                Set<String> actualHeaders = csvParser.getHeaderMap().keySet();
                System.out.println("Headers found: " + actualHeaders);

                if (!actualHeaders.containsAll(expectedHeaders)) {
                    throw new IllegalArgumentException("CSV file is missing required headers: " + expectedHeaders);
                }

                // Process each record
                for (CSVRecord csvRecord : csvParser) {
                    System.out.println("Processing record: " + csvRecord.toString());

                    ApplicationCodesetDTO appCode = new ApplicationCodesetDTO();
                    appCode.setDisplay(csvRecord.get("display").trim());
                    appCode.setDescription(csvRecord.get("description").trim());
                    appCode.setCodesetGroup(csvRecord.get("codeset_group").trim());
                    appCode.setCode(csvRecord.get("code").trim());
                    appCode.setArchived(Integer.parseInt(csvRecord.get("archived").trim()));
                    appCode.setVersion(csvRecord.get("version").trim());

                    listOfCodesets.add(appCode);
                }

                System.out.println("Finished parsing CSV. Records found: " + listOfCodesets.size());
            } catch (IOException e) {
                System.err.println("Error processing CSV file: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("An error occurred while processing the file.", e);
            }
        } else if (fileType.equalsIgnoreCase("json")) {
            try {
                listOfCodesets = Arrays.asList(objectMapper.readValue(file.getInputStream(), ApplicationCodesetDTO[].class));
                System.out.println("Parsed JSON records: " + listOfCodesets.size());
            } catch (JsonMappingException | JsonParseException e) {
                System.err.println("Error processing JSON file: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("An error occurred while processing the JSON file.", e);
            }
        }

        System.out.println("Parsed codesets: " + listOfCodesets);
        return listOfCodesets;
    }

    @Transactional
    @Override
    public List<ApplicationCodesetDTO> saveCodesets(List<ApplicationCodesetDTO> codesetDTOList) {
        if (codesetDTOList == null || codesetDTOList.isEmpty()) {
            System.out.println("No codesets to save");
            return Collections.emptyList();
        }

        System.out.println("Attempting to save " + codesetDTOList.size() + " codesets");
        List<BaseApplicationCodeset> codesetsToBeSaved = new ArrayList<>();

        for (ApplicationCodesetDTO dto : codesetDTOList) {
            // Check if an entity with the same code already exists
            Optional<BaseApplicationCodeset> existingCode = applicationCodesetRepository.findByCode(dto.getCode());

            if (existingCode.isPresent()) {
                // Update existing entity
                System.out.println("Updating existing codeset: " + dto.getCode());
                BaseApplicationCodeset existingAppCodeset = existingCode.get();
                existingAppCodeset.setVersion(dto.getVersion());
                existingAppCodeset.setCodesetGroup(dto.getCodesetGroup());
                existingAppCodeset.setDisplay(dto.getDisplay());
                existingAppCodeset.setDescription(dto.getDescription());
                existingAppCodeset.setArchived(dto.getArchived());
                codesetsToBeSaved.add(existingAppCodeset);
            } else {
                // Create a new entity
                System.out.println("Creating new codeset: " + dto.getCode());
                BaseApplicationCodeset newAppCodeset = BaseApplicationCodeset.builder()
                        .code(dto.getCode())
                        .version(dto.getVersion())
                        .codesetGroup(dto.getCodesetGroup())
                        .display(dto.getDisplay())
                        .description(dto.getDescription())
                        .archived(dto.getArchived())
                        .build();
                codesetsToBeSaved.add(newAppCodeset);
            }
        }

        // Save all entities to the database
        try {
            List<BaseApplicationCodeset> savedCodesets = applicationCodesetRepository.saveAll(codesetsToBeSaved);
            System.out.println("Saved codesets: " + savedCodesets.size());
            return savedCodesets.stream()
                    .map(ApplicationCodesetDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error saving codesets: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving codesets to the database.", e);
        }
    }
}