package com.pje.kelompok4.controller;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import com.pje.kelompok4.model.Bagian;
import com.pje.kelompok4.payload.request.BagianReq;
import com.pje.kelompok4.payload.request.BagianUpdateReq;
import com.pje.kelompok4.payload.response.ResponseDto;
import com.pje.kelompok4.repositoriy.BagianRepo;

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
@RequestMapping("/api/bagian")
public class BagianController {
    @Autowired
    BagianRepo bagianRepo;

    @PostMapping("/create")
	public ResponseEntity<?> createBagian(@Valid @RequestBody BagianReq bagianReq) {
		int code   = 201;
		String msg = "bagian baru berhasil dibuat";

		try {
			if (bagianRepo.existsByName(bagianReq.getName())) {
				code = 400;
				msg  = "nama bagian ini sudah dipakai"; 
			}
			else {
				Bagian bagian  = new Bagian();
				Date date      = new Date();
				String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
				
				bagian.setName(bagianReq.getName());
				bagian.setDescription(bagianReq.getDescription());
				bagian.setCreatedAt(strDate);
				bagianRepo.save(bagian);
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
	public ResponseEntity<?> updateBagian(@Valid @RequestBody BagianUpdateReq bUpdateReq) {
		int code   = 201;
		String msg = "bagian dengan id {"+bUpdateReq.getId()+"} berihasil diedit";

		try {
			// Check id is exist
			if (bagianRepo.existsById(bUpdateReq.getId()) == false) {
				code = 400;
				msg  = "bagian dengan id {"+bUpdateReq.getId()+"} tidak ditemukan"; 
			}
			else {
				// Check name is exist
				Bagian result = bagianRepo.validateName(bUpdateReq.getName(), bUpdateReq.getId());
				
				if (Objects.isNull(result) == false) {
					code = 400;
					msg  = "nama bagian ini sudah dipakai";
				}
				else {
					Bagian bagian  = bagianRepo.findBagianById(bUpdateReq.getId());
					Date date      = new Date();
					String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
					
					bagian.setName(bUpdateReq.getName());
					bagian.setDescription(bUpdateReq.getDescription());
					bagian.setUpdatedAt(strDate);
					bagianRepo.save(bagian);
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
    public ResponseEntity<?> deleteBagian(@PathVariable Long id) {
		int code   = 201;
		String msg = "bagian dengan id {"+id+"} berhasil dihapus";

        try {
            if (bagianRepo.existsById(id) == false) {
				code = 400;
				msg  = "bagian dengan id {"+id+"} tidak ditemukan";
			}
			else {
				bagianRepo.deleteById(id);
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
	public ResponseEntity<?> getBagian(@RequestParam(required = false) Long id) {
		try {
			if (Objects.isNull(id) == false) {
				Bagian bagian = bagianRepo.findBagianById(id);
		
				ResponseDto<?> responseData = new ResponseDto<>(
					(Objects.isNull(bagian)) ? 404 : 200,
					(Objects.isNull(bagian)) ? "failed" : "success",
					(Objects.isNull(bagian)) ? "bagian dengan id {"+id+"} tidak ditemukan" : bagian
				);
				
				return ResponseEntity
					.status(HttpStatus.OK)
					.body(responseData);
			} 
			else {
				Iterable<Bagian> listBagian = bagianRepo.findAll();
		
				ResponseDto<?> responseData = new ResponseDto<>(
					200,
					"success",
					listBagian
				);

				return ResponseEntity
					.status(HttpStatus.OK)
					.body(responseData);
			}
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
