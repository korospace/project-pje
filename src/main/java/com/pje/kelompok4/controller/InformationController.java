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
		int code   = 201;
		String msg = "informasi baru berhasil dibuat";

		try {
			if (iRepo.existsByName(iReq.getName())) {
				code = 400;
				msg  = "nama info ini sudah dipakai"; 
			}
			else {
				Information information = new Information();
				Date date      = new Date();
				String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
				
				information.setName(iReq.getName());
				information.setDescription(iReq.getDescription());
				information.setCreatedAt(strDate);
				iRepo.save(information);
			}
        } 
        catch (Exception e) {
			code = 500;
			msg  = e.getMessage();
        }

		return ResponseEntity
			.status(code)
			.body(new ResponseDto<>(
				code,
				(code < 300) ? "success" : "failed",
				msg
			));
	}

    @PutMapping("/update")
	public ResponseEntity<?> updateInfo(@Valid @RequestBody InformationUpdateReq iUpdateReq) {
		int code   = 201;
		String msg = "info dengan id {"+iUpdateReq.getId()+"} berihasil diedit";

		try {
			// Check id is exist
			if (iRepo.existsById(iUpdateReq.getId()) == false) {
				code = 400;
				msg  = "info dengan id {"+iUpdateReq.getId()+"} tidak ditemukan"; 
			}
			else {
				// Check name is exist
				Information result = iRepo.validateName(iUpdateReq.getName(), iUpdateReq.getId());
				
				if (Objects.isNull(result) == false) {
					code = 400;
					msg  = "nama info ini sudah dipakai"; 
				}
				else {
					Information information = iRepo.findInformationById(iUpdateReq.getId());
					Date date      = new Date();
					String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
					
					information.setName(iUpdateReq.getName());
					information.setDescription(iUpdateReq.getDescription());
					information.setUpdatedAt(strDate);
					iRepo.save(information);
				}
			}
        } 
        catch (Exception e) {
			code = 500;
			msg  = e.getMessage();
        }

		return ResponseEntity
			.status(code)
			.body(new ResponseDto<>(
				code,
				(code < 300) ? "success" : "failed",
				msg
			));
	}

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInfo(@PathVariable Long id) {
		int code   = 201;
		String msg = "info dengan id {"+id+"} berhasil dihapus";

        try {
            if (iRepo.existsById(id) == false) {
				code = 400;
				msg  = "info dengan id {"+id+"} tidak ditemukan";
			}
			else {
				iRepo.deleteById(id);
			}
        } 
        catch (Exception e) {
			code = 500;
			msg  = e.getMessage();
        }

		return ResponseEntity
			.status(code)
			.body(new ResponseDto<>(
				code,
				(code < 300) ? "success" : "failed",
				msg
			));
    }

    @GetMapping
	public ResponseEntity<?> getInfo(@RequestParam(required = false) String name) {
		try {
			ResponseDto<?> responseData;
			int status;

			if (Objects.isNull(name) == false) {
				Information result = iRepo.findInformationByName(name);
				status = (Objects.isNull(result)) ? 404 : 200;
				
				responseData = new ResponseDto<>(
					status,
					(Objects.isNull(result)) ? "failed" : "success",
					(Objects.isNull(result)) ? "" : result
				);
			}
			else {
				status = 200;
				Iterable<InformationRes> result = iRepo.getAllInformations();
			
				responseData = new ResponseDto<>(
					status,
					"success",
					result
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
