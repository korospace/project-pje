package com.pje.kelompok4.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.pje.kelompok4.model.User;
import com.pje.kelompok4.payload.request.UserLoginReq;
import com.pje.kelompok4.payload.request.UserCreateReq;
import com.pje.kelompok4.payload.request.UserProfileReq;
import com.pje.kelompok4.payload.request.UserUpdateReq;
import com.pje.kelompok4.payload.response.JwtResponse;
import com.pje.kelompok4.payload.response.ResponseDto;
import com.pje.kelompok4.payload.response.UserRes;
import com.pje.kelompok4.repositoriy.UserRepo;
import com.pje.kelompok4.security.jwt.JwtUtils;
import com.pje.kelompok4.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepo userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginReq userLoginReq) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userLoginReq.getUsername(), userLoginReq.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		ResponseDto<?> responseData = new ResponseDto<>(
			200,
			"success",
			new JwtResponse(
				jwt, 
				userDetails.getId(), 
				userDetails.getUsername(), 
				roles
			)
		);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(responseData);
	}

	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateReq uCreateReq) {
		try {
			ResponseDto<?> responseData;
			int code   = 201;
			String msg = "akun berhasil dibuat";

			if (userRepository.existsByNik(uCreateReq.getNik())) {
				code = 400;
				msg  = "nik is already taken";
			}
			else if (userRepository.existsByUsername(uCreateReq.getUsername())) {
				code = 400;
				msg  = "username is already taken";
			}

			String role;
			switch (uCreateReq.getRole()) {
				case "admin":
					role = "ROLE_ADMIN";
					break;
				default:
					role = "ROLE_PEGAWAI";
			}
			
			if (code < 300) {
				User user 	   = new User();
				Date date      = new Date();
				String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);

				user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				user.setNik(uCreateReq.getNik());
				user.setUsername(uCreateReq.getUsername());
				user.setPassword(encoder.encode(uCreateReq.getPassword()));
				user.setRole(role);
				user.setCreatedAt(strDate);
				userRepository.save(user);
			}
	
			responseData = new ResponseDto<>(
				code,
				(code < 300) ? "success" : "failed",
				msg
			);

			return ResponseEntity
				.status(code)
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
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateReq userUpdateReq,HttpServletRequest request) {
		try {
			ResponseDto<?> responseData;
			int code   = 201;
			String msg = "akun berhasil diedit";

			String headerAuth = request.getHeader("token");
			String uniqueId   = jwtUtils.getUniqueIdromJwtToken(
				headerAuth.substring(4, headerAuth.length())
			);

			// Check id is exist
			User result1 = userRepository.existsByIdExcept(userUpdateReq.getId(),uniqueId);
			
			if (Objects.isNull(result1)) {
				code = 400;
				msg  = "user dengan id {"+userUpdateReq.getId()+"} tidak ditemukan";
			}
			else {
				// Check username is exist
				User result2 = userRepository.validateUsername(
					userUpdateReq.getUsername(), userUpdateReq.getId()
				);

				if (Objects.isNull(result2) == false) {
					code = 400;
					msg  = "username ini sudah dipakai";
				}
				else {
					// Check nik is exist
					User result3 = userRepository.validateNik(
						userUpdateReq.getNik(), userUpdateReq.getId()
					);

					if (Objects.isNull(result3) == false) {
						code = 400;
						msg  = "nik ini sudah dipakai";
					}
				}
			}

			String role;
			switch (userUpdateReq.getRole()) {
				case "admin":
					role = "ROLE_ADMIN";
					break;
				default:
					role = "ROLE_PEGAWAI";
			}

			if (code < 300) {
				User user = userRepository.findById(userUpdateReq.getId());
				Date date      = new Date();
				String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
				
				user.setNik(userUpdateReq.getNik());
				user.setUsername(userUpdateReq.getUsername());
				if (!Objects.isNull(userUpdateReq.getPassword())) {
					user.setPassword(encoder.encode(userUpdateReq.getPassword()));
				}
				user.setRole(role);
				user.setUpdatedAt(strDate);
				userRepository.save(user);
			}
				
			responseData = new ResponseDto<>(
				code,
				(code < 300) ? "success" : "failed",
                msg
			);

			return ResponseEntity
				.status(code)
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

	@PutMapping("/update_profile")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PEGAWAI')")
	public ResponseEntity<?> updateProfile(@Valid @RequestBody UserProfileReq uProfileReq,HttpServletRequest request) {
		try {
			ResponseDto<?> responseData;
			int code   = 201;
			String msg = "profile berhasil diedit";
			
			String headerAuth = request.getHeader("token");
			String uniqueId   = jwtUtils.getUniqueIdromJwtToken(
				headerAuth.substring(4, headerAuth.length())
			);

			// Check nik is exist
			User result1 = userRepository.validateNik(uProfileReq.getNik(), uniqueId);

			if (Objects.isNull(result1) == false) {
				code = 400;
				msg  = "nik is already taken";
			}
			else {
				// Check username is exist
				User result2 = userRepository.validateUsername(uProfileReq.getUsername(), uniqueId);
	
				if (Objects.isNull(result2) == false) {
					code = 400;
					msg  = "username is already taken";
				}
			}

			if(code < 300) {
				User user      = userRepository.findById(uniqueId);
				Date date      = new Date();
				String strDate = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(date);
				
				user.setNik(uProfileReq.getNik());
				user.setUsername(uProfileReq.getUsername());
				if (!Objects.isNull(uProfileReq.getPassword())) {
					user.setPassword(encoder.encode(uProfileReq.getPassword()));
				}
				user.setUpdatedAt(strDate);
				userRepository.save(user);
			}
				
			responseData = new ResponseDto<>(
				code,
				(code < 300) ? "success" : "failed",
				msg
			);

			return ResponseEntity
				.status(code)
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
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            if (userRepository.existsById(id) == false) {
				ResponseDto<?> responseData = new ResponseDto<>(
					400,
					"failed",
					"user dengan id {"+id+"} tidak ditemukan"
				);

				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(responseData);
			}

            userRepository.deleteUserById(id);;
            
            ResponseDto<?> responseData = new ResponseDto<>(
				200,
				"success",
				"user dengan id {"+id+"} berhasil dihapus"
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
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getUser(@RequestParam(required = false) String id,HttpServletRequest request) {
		try {
			ResponseDto<?> responseData;
			int code = 200;

			String headerAuth = request.getHeader("token");
			String uniqueId   = jwtUtils.getUniqueIdromJwtToken(
				headerAuth.substring(4, headerAuth.length())
			);

			if (Objects.isNull(id)) {
				code = 200;
				Iterable<UserRes> result = userRepository.getAllUsersExcept(uniqueId);
			
				responseData = new ResponseDto<>(
					code,
					"success",
					result
				);
			}
			else {
				User result = userRepository.findByIdExcept(id,uniqueId);
				code = (Objects.isNull(result)) ? 404 : 200;
				
				responseData = new ResponseDto<>(
					code,
					(Objects.isNull(result)) ? "failed" : "success",
					(Objects.isNull(result)) ? "" : result
				);
			}

			return ResponseEntity
				.status(code)
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
