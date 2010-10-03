package org.dubh.islay.hub.server.facebook;

import java.util.Date;
import java.util.List;

import com.google.common.base.Objects;
import static com.google.common.base.Objects.equal;

public class Post implements FacebookObject {


  private String id;
  private User from;
  private User to;
  private String message;
  private String picture;
  private String link;
  private String name;
  private String caption;
  private String description;
  private String source;
  private String icon;
  private String attribution;
  private int likes;
  private Date created_time;
  private Date updated_time;
  private List<Post> comments;
  
  @Override
  public String getId() {
    return id;
  }

  public User getFrom() {
    return from;
  }

  public Post setFrom(User from) {
    this.from = from;
    return this;
  }

  public User getTo() {
    return to;
  }

  public Post setTo(User to) {
    this.to = to;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public Post setMessage(String message) {
    this.message = message;
    return this;
  }

  public String getPicture() {
    return picture;
  }

  public Post setPicture(String picture) {
    this.picture = picture;
    return this;
  }

  public String getLink() {
    return link;
  }

  public Post setLink(String link) {
    this.link = link;
    return this;
  }

  public String getName() {
    return name;
  }

  public Post setName(String name) {
    this.name = name;
    return this;
  }

  public String getCaption() {
    return caption;
  }

  public Post setCaption(String caption) {
    this.caption = caption;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Post setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getSource() {
    return source;
  }

  public Post setSource(String source) {
    this.source = source;
    return this;
  }

  public String getIcon() {
    return icon;
  }

  public Post setIcon(String icon) {
    this.icon = icon;
    return this;
  }

  public String getAttribution() {
    return attribution;
  }

  public Post setAttribution(String attribution) {
    this.attribution = attribution;
    return this;
  }

  public int getLikes() {
    return likes;
  }

  public Post setLikes(int likes) {
    this.likes = likes;
    return this;
  }

  public Date getCreatedTime() {
    return created_time;
  }

  public Post setCreatedTime(Date created_time) {
    this.created_time = created_time;
    return this;
  }

  public Date getUpdatedTime() {
    return updated_time;
  }

  public Post setUpdatedTime(Date updated_time) {
    this.updated_time = updated_time;
    return this;
  }

  public List<Post> getComments() {
    return comments;
  }

  public Post setComments(List<Post> comments) {
    this.comments = comments;
    return this;
  }

  public Post setId(String id) {
    this.id = id;
    return this;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
  
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Post)) {
      return false;
    }
    Post other = (Post) o;
    return equal(attribution, other.attribution) &&
        equal(caption, other.caption) &&
        equal(comments, other.comments) &&
        equal(created_time, other.created_time) &&
        equal(description, other.description) &&
        equal(from, other.from) &&
        equal(icon, other.icon) &&
        equal(id, other.id) &&
        likes == other.likes &&
        equal(link, other.link) &&
        equal(message, other.message) &&
        equal(name, other.name) &&
        equal(picture, other.picture) &&
        equal(source, other.source) &&
        equal(to, other.to) &&
        equal(updated_time, other.updated_time);
  }
  
  @Override
  public String toString() {
    return "Post [id=" + id + ", from=" + from + ", to=" + to + ", message=" + message
        + ", picture=" + picture + ", link=" + link + ", name=" + name + ", caption=" + caption
        + ", description=" + description + ", source=" + source + ", icon=" + icon
        + ", attribution=" + attribution + ", likes=" + likes + ", created_time=" + created_time
        + ", updated_time=" + updated_time + ", comments=" + comments + "]";
  }
}
