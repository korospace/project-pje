package com.pje.kelompok4.controller;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import com.pje.kelompok4.model.Information;
import com.pje.kelompok4.payload.request.InformationReq;
import com.pje.kelompok4.payload.request.InformationUpdateReq;
import com.pje.kelompok4.payload.response.InformationRes;
import com.pje.kelompok4.payload.response.ResponseDto;
import com.pje.kelompok4.repositoriy.InformationRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/information")
public class InformationController {
    @Autowired
    InformationRepo iRepo;

    @PostMapping("/create")
	public ResponseEntity<?> createInfo(@Valid @RequestBody InformationReq iReq) {
		try {
			if (iRepo.existsByName(iReq.getName())) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"nama info ini sudah dipakai!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}
	
			Information information = new Information();
			Date date      = new Date();
			String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);

            information.setName(iReq.getName());
            information.setDescription(iReq.getDescription());
			information.setCreatedAt(strDate);
			iRepo.save(information);
	
			ResponseDto<?> responseData = new ResponseDto<>(
				201,
				"success",
				"informasi baru berhasil dibuat!"
			);

			return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(responseData);
        } 
        catch (Exception e) {
            ResponseDto<?> responseData = new ResponseDto<>(
				500,
				"failed",
				e.getMessage()
			);

            return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(responseData);
        }
	}

    @PutMapping("/update")
	public ResponseEntity<?> updateInfo(@Valid @RequestBody InformationUpdateReq iUpdateReq) {
		try {
			// Check id is exist
			if (iRepo.existsById(iUpdateReq.getId()) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"info dengan id {"+iUpdateReq.getId()+"} tidak ditemukan!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}

			// Check name is exist
			Information result = iRepo.validateName(iUpdateReq.getName(), iUpdateReq.getId());

			if (Objects.isNull(result) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"nama info ini sudah dipakai!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}
	
			Information information = iRepo.findInformationById(iUpdateReq.getId());
			Date date      = new Date();
			String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
			
            information.setName(iUpdateReq.getName());
            information.setDescription(iUpdateReq.getDescription());
			information.setUpdatedAt(strDate);
			iRepo.save(information);
				
			ResponseDto<?> responseData = new ResponseDto<>(
				201,
				"success",
                "info dengan id {"+iUpdateReq.getId()+"} berihasil diedit!"
			);

			return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(responseData);
        } 
        catch (Exception e) {
            ResponseDto<?> responseData = new ResponseDto<>(
				500,
				"failed",
				e.getMessage()
			);

            return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(responseData);
        }
	}

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInfo(@PathVariable Long id) {
        try {
            if (iRepo.existsById(id) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"info dengan id {"+id+"} tidak ditemukan!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}

            iRepo.deleteById(id);
            
            ResponseDto<?> responseData = new ResponseDto<>(
				200,
				"success",
				"info dengan id {"+id+"} berhasil dihapus!"
			);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
        } 
        catch (Exception e) {
            ResponseDto<?> responseData = new ResponseDto<>(
				500,
				"failed",
				e.getMessage()
			);

            return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(responseData);
        }
    }

    @GetMapping
	public ResponseEntity<?> getInfo(@RequestParam(required = false) String name) {
		try {
			ResponseDto<?> responseData;
			int status;

			if (Objects.isNull(name)) {
				status = 200;
				Iterable<InformationRes> result = iRepo.getAllInformations();
			
				responseData = new ResponseDto<>(
					status,
					"success",
					result
				);
			}
			else {
				Information result = iRepo.findInformationByName(name);
				status = (Objects.isNull(result)) ? 404 : 200;
				
				responseData = new ResponseDto<>(
					status,
					(Objects.isNull(result)) ? "failed" : "success",
					(Objects.isNull(result)) ? "" : result
				);
			}

			return ResponseEntity
				.status(status)
				.body(responseData);
        } 
        catch (Exception e) {
            ResponseDto<?> responseData = new ResponseDto<>(
				500,
				"failed",
				e.getMessage()
			);

            return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(responseData);
        }
	}
}
