package clase;

public class Usuario {

    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String dni;
    private String telefono;
    private String password;
    private String rol;
    private int puntos;

    public Usuario(int id, String nombre, String apellido,
            String email, String dni, String telefono, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.dni = dni;
        this.telefono = telefono;
        this.password = password;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }
    
    public String getDni() {
        return dni;
    }
    
    public String getTelefono() {
        return telefono;
    }

    public String getPassword() {
        return password;
    }

    public String getRol() {
        return rol;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public int getPuntos()          { return puntos; }
    public void setPuntos(int pts)  { this.puntos = pts; }
}
