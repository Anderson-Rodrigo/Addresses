package com.example.addresses.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "uuid", updatable = false)
	@Setter(AccessLevel.NONE)
	private UUID id;

	@Column(name = "street_name")
	@Size(max = 255)
	@NotEmpty
	private String streetName;

	@Column(name = "number")
	@Size(max = 255)
	@NotEmpty
	private String number;

	@Column(name = "complement")
	@Size(max = 255)
	private String complement;

	@Column(name = "neighbourhood")
	@Size(max = 255)
	@NotEmpty
	private String neighbourhood;

	@Column(name = "city")
	@Size(max = 255)
	@NotEmpty
	private String city;

	@Column(name = "state")
	@Size(max = 255)
	@NotEmpty
	private String state;

	@Column(name = "country")
	@Size(max = 255)
	@NotEmpty
	private String country;

	@Column(name = "zipcode")
	@Size(max = 255)
	@NotEmpty
	private String zipcode;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@CreationTimestamp
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", insertable = false)
	private Date updatedAt;

}
