package com.slavov.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.slavov.model.Payment;
import com.slavov.repository.PaymentRepository;

@RestController
@RequestMapping("/payments")
public class PaymentController {
	
	/** 
	 * Defines the serialization of payment to TSV file.
	 * Clients (JavaScript) should use the same formatter for parsing dates and numbers.
	 */
	private static final String TSV_ROW_FORMAT = "%s\t%.2f";
	private static final SimpleDateFormat TSV_DATE_FORMAT = new SimpleDateFormat( "d-MMM-yy" );
	
	private static final Comparator<Payment> COMPARATOR_BY_DATЕ = new Comparator<Payment>() {

		@Override
		public int compare(Payment o1, Payment o2) {
			return o1.getDate().compareTo(o2.getDate());
		}
		
	};
	
	@Autowired
	private PaymentRepository repo;

	@RequestMapping(method = RequestMethod.GET)
	public List<Payment> findPayments() {
		return repo.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public Payment addPayment(@RequestBody Payment payment) {
		payment.setId(null);
		payment.setDate( new Date() );
		return repo.saveAndFlush(payment);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Payment updatePayment(@RequestBody Payment updatedPayment, @PathVariable Integer id) {
		updatedPayment.setId(id);
		return repo.saveAndFlush(updatedPayment);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deletePayment(@PathVariable Integer id) {
		repo.delete(id);
	}
	
	@RequestMapping(value = "/tsv", method = RequestMethod.GET, produces = "text/tsv")
	public String exportPaymentsToTsv() {
		StringBuilder result = new StringBuilder();
		
		result.append( "date\tamount\n" );
		
		result.append( repo.findAll().stream()
					.sorted( COMPARATOR_BY_DATЕ )
					.map( p -> String.format( TSV_ROW_FORMAT,
							TSV_DATE_FORMAT.format( p.getDate() ), 
							p.getAmount() ) )
					.collect( Collectors.joining( "\n" ) ) );
		
		return result.toString();
	}

	/**
	 * Create dummy data
	 */
	@RequestMapping(value = "/addTestData", method = RequestMethod.GET)
	public void addTestData() {
		Random random = new Random();
		
		for (int i = 0; i < 99; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.add( Calendar.DAY_OF_YEAR, random.nextInt( 100 ) ); // 100 days
			
			Payment payment = new Payment();
			payment.setDescription( "Payment " + i );
			payment.setDate( calendar.getTime() );
			payment.setAmount( random.nextDouble() * 10 );
			repo.saveAndFlush(payment );
		}
	}
}
