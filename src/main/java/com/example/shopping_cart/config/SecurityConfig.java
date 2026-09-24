package com.example.shopping_cart.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.shopping_cart.security.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtFilter jwtFilter;
	

	public SecurityConfig(JwtFilter jwtFilter) {

		this.jwtFilter = jwtFilter;
	}
	
//	Spring自己寫的class
//	應用程式啟動時，執行一次
	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		
		http
		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
//		關閉CSRF 防護
		.csrf(csrf -> csrf.disable())
//		   TODO 1
//		關閉 Spring Security 內建的表單登入功能
				.formLogin(form -> form.disable())
//		   TODO 2
//				關閉 HTTP Basic 驗證
				.httpBasic(basic -> basic.disable())
//		   TODO 3
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/error").permitAll()
//				   requestMatchers() 回傳的是「等待指定規則」的物件
//				   permitAll() 意思是所有人都能存取，不用登入
						
//						要開放給未登入者的（permitAll）→ 要主動寫
//						只給管理員的（hasRole）→ 要主動寫
//						同一個路徑，不同 method 要給不同權限時，才需要加 method 區分，而且順序很重要
//						窄的開放規則放前面，寬的限制規則放後面
						
						// member
						.requestMatchers("/api/member/register", "/api/member/login").permitAll()
						// 商品前台
//						未登入的人可以查看商品，那規則就寫成GET可以放行
						.requestMatchers(HttpMethod.GET, "/api/product/findall", "/api/product/{productID}").permitAll()
						.requestMatchers("/api/auth/**").permitAll()
						// 商品後台，** 不管路徑後面接什麼都能匹配到
//		 hasRole：必須是指定角色才能存取，不指定method，任何method都要 ADMIN（限制的規則寫寬）
						.requestMatchers("/api/product/add/**","/api/product/update/**","/api/product/delete/**").hasRole("ADMIN")
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
//		.anyRequest().authenticated()上面規則都沒匹配到的所有其他請求，只要已登入就能存取，不管角色
//		購物車和訂單不需要寫，下面程式碼自動變成「需要登入」
						.anyRequest().authenticated()

				);
//		把 A 插在 B 之前
//它是 Spring Security 處理表單登入的 Filter(SecurityConfig 裡當定位點)，位置在授權檢查之前。插在它前面，就能確保順序		
     http.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
//		允許哪些來源，跟你原本 WebConfig 的設定一樣
		config.setAllowedOrigins(List.of("http://localhost:5173"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS"));
//		允許前端帶任何header，包括之後的Authorization
		config.setAllowedHeaders(List.of("*"));
//		允許帶cookie，refresh token 需要它
		config.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		設定套用到所有路徑
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() { //介面
//		PasswordEncoder 被當成回傳型別，卻沒有 new PasswordEncoder()，這是介面的典型用法
		return new BCryptPasswordEncoder(); //實作
	}
}


//.sessionManagement(...)：設定 Session 管理
//session：Spring 傳入的 Session 設定物件
//.sessionCreationPolicy(...)：設定「什麼時候建立 Session」
//SessionCreationPolicy.STATELESS：永遠不建立，也不使用

//authorizeHttpRequests：授權規則的開頭
//auth：Spring 傳入的規則清單物件


//Security 規則裡的意思是「這一層放任何文字都可以」，它是一個萬用位置
//Security 只比對網址的形狀，不知道 findall 是查全部、5 是查單筆。
//它們是不是同一個功能，是之後 Spring MVC 根據你的 @GetMapping 決定

//Security 只看網址形狀，所以規則要寫得夠精確，才能表達你真正的意圖

//permitAll()	所有人
//authenticated()	已登入
//hasRole("ADMIN")	角色是 ADMIN（自動補 ROLE_ 前綴）
//hasAnyRole("ADMIN", "STAFF")	符合其中一個角色
//hasAuthority("ROLE_ADMIN")	比對完整權限字串，不自動補前綴
//denyAll()	所有人都不行

//-------------------------------------
//securityFilterChain 方法裡直接呼叫了corsConfigurationSource()。
//這在 @Configuration class 裡是安全的：Spring 會攔截這個呼叫，
//回傳已經建立好的同一個 Bean，不會重新 new 一個。這是 @Configuration 
//的特殊處理（叫 proxyBeanMethods），一般 class 沒有這個行為。
