package kz.milairis.common.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity has many-to-many relationship with a brand entity.
 * Entity has one-to-one relationship and one-to-many. It can have one parent and many children.
 * allParentIDs field is set of parents of particular category. It is using to search for products
 * in a specific category, including its subcategories.
 */
@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String name;
	
	@Column(length = 64, nullable = false, unique = true)
	private String alias;
	
	@Column(length = 128, nullable = false)
	private String image;
	
	private boolean enabled;

	@Column(name = "all_parent_ids", length = 256, nullable = true)
	private String allParentIDs;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();

	public Category() {
	}
	
	public Category(Integer id) {
		this.id = id;
	}

	/**
	 * Method is using in forms when user want to choose one category.
	 * It returns list of copies. And does not contain unnecessary information of category.
	 */
	public static Category copyIdAndName(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		
		return copyCategory;
	}

	/**
	 * Method is using in forms when user want to choose one category.
	 * It returns list of copies. And does not contain unnecessary information of category.
	 */
	public static Category copyIdAndName(Integer id, String name) {
		Category copyCategory = new Category();
		copyCategory.setId(id);
		copyCategory.setName(name);
		
		return copyCategory;
	}

	/**
	 * Method is using in category pages when user want to manage categories page.
	 * It returns list of copies. And contains full information of category.
	 */
	public static Category copyFull(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setImage(category.getImage());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setHasChildren(category.getChildren().size() > 0);
		
		return copyCategory;		
	}

	/**
	 * Method is using in category pages when user want to manage categories page.
	 * It returns list of copies. And contains all information of category.
	 */
	public static Category copyFull(Category category, String name) {
		Category copyCategory = Category.copyFull(category);
		copyCategory.setName(name);
		
		return copyCategory;
	}

	public Category(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}
	
	public Category(String name, Category parent) {
		this(name);
		this.parent = parent;
	}	

	public Category(Integer id, String name, String alias) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	@Transient
	public String getImagePath() {
		if (this.id == null) return "/images/image-thumbnail.png";
		
		return "/category-images/" + this.id + "/" + this.image;
	}
	
	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@Transient
	private boolean hasChildren;

	@Override
	public String toString() {
		return this.name;
	}

	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}
}
