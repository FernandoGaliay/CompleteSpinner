package completespinnertest.sdos.es.completespinnertest;

/**
 * Created by fernando.galiay on 26/10/2015.
 */
public class SpinnerVO {

    private Integer id;

    private String titulo;

    public SpinnerVO(Integer id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return getTitulo();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SpinnerVO && this.getId().equals(((SpinnerVO)o).getId());
    }
}