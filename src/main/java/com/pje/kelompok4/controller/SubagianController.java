package com.pje.kelompok4.controller;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import com.pje.kelompok4.model.Subagian;
import com.pje.kelompok4.payload.request.SubagianReq;
import com.pje.kelompok4.payload.request.SubagianUpdateReq;
import com.pje.kelompok4.payload.response.ResponseDto;
import com.pje.kelompok4.repositoriy.BagianRepo;
import com.pje.kelompok4.repositoriy.SubagianRepo;

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
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/subagian")
public class SubagianController {
    @Autowired
    BagianRepo bagianRepo;

	@Autowired
	SubagianRepo subagianRepo;

    @PostMapping("/create")
	public ResponseEntity<?> createSubagian(@Valid @RequestBody SubagianReq subagianReq) {
		try {
			if (subagianRepo.existsByName(subagianReq.getName())) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"nama subagian ini sudah dipakai!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}
			else if (bagianRepo.existsById(subagianReq.getIdBagian()) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"id bagian {"+subagianReq.getIdBagian()+"} tidak terdaftar!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}
	
			Subagian subagian = new Subagian();
			Date date         = new Date();
			String strDate    = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);

            subagian.setName(subagianReq.getName());
            subagian.setDescription(subagianReq.getDescription());
			subagian.setIdBagian(subagianReq.getIdBagian());
			subagian.setCreatedAt(strDate);
			subagianRepo.save(subagian);
	
			ResponseDto<?> responseData = new ResponseDto<>(
				201,
				"success",
				"subagian baru berhasil dibuat!"
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
	public ResponseEntity<?> updateSubagian(@Valid @RequestBody SubagianUpdateReq sbUpdateReq) {
		try {
			// Check id is exist
			if (subagianRepo.existsById(sbUpdateReq.getId()) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"subagian dengan id {"+sbUpdateReq.getId()+"} tidak ditemukan!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}
			
			// Check name is exist
			Subagian result = subagianRepo.validateName(sbUpdateReq.getName(), sbUpdateReq.getId());

			if (Objects.isNull(result) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"nama subagian ini sudah dipakai!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}

			// Check id_bagian is exist
			if (bagianRepo.existsById(sbUpdateReq.getIdBagian()) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"id bagian {"+sbUpdateReq.getIdBagian()+"} tidak terdaftar!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}
	
			Subagian subagian = subagianRepo.findSubagianById(sbUpdateReq.getId());
			Date date         = new Date();
			String strDate    = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
			
            subagian.setName(sbUpdateReq.getName());
            subagian.setDescription(sbUpdateReq.getDescription());
			subagian.setIdBagian(sbUpdateReq.getIdBagian());
			subagian.setUpdatedAt(strDate);
			subagianRepo.save(subagian);
				
			ResponseDto<?> responseData = new ResponseDto<>(
				201,
				"success",
				// updateUserReq
                "subagian dengan id {"+sbUpdateReq.getId()+"} berihasil diedit!"
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
    public ResponseEntity<?> deleteSubagian(@PathVariable Long id) {
        try {
            if (subagianRepo.existsById(id) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"subagian dengan id {"+id+"} tidak ditemukan!"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}

            subagianRepo.deleteById(id);
            
            ResponseDto<?> responseData = new ResponseDto<>(
					200,
					"success",
					"subagian dengan id {"+id+"} berhasil dihapus!"
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
	public ResponseEntity<?> getSubagian() {
		try {
            Iterable<Subagian> listSubagian = subagianRepo.findAll();
	
			ResponseDto<?> responseData = new ResponseDto<>(
				200,
				"success",
				listSubagian
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
}
