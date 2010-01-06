package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

// Generated 06/01/2010 5:09:20 PM by Hibernate Tools 3.2.5.Beta

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * RigCapabilities generated by hbm2java
 */
@Entity
@Table(name = "rig_capabilities", catalog = "sahara")
public class RigCapabilities implements java.io.Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 7099369236095416135L;
    private Long id;
    private String capabilities;
    private Set<MatchingCapabilities> matchingCapabilitieses = new HashSet<MatchingCapabilities>(
            0);
    private Set<Rig> rigs = new HashSet<Rig>(0);
    private Set<MatchingCapabilities> matchingCapabilitieses_1 = new HashSet<MatchingCapabilities>(
            0);
    private Set<Rig> rigs_1 = new HashSet<Rig>(0);

    public RigCapabilities()
    {
    }

    public RigCapabilities(final String capabilities)
    {
        this.capabilities = capabilities;
    }

    public RigCapabilities(final String capabilities,
            final Set<MatchingCapabilities> matchingCapabilitieses,
            final Set<Rig> rigs,
            final Set<MatchingCapabilities> matchingCapabilitieses_1,
            final Set<Rig> rigs_1)
    {
        this.capabilities = capabilities;
        this.matchingCapabilitieses = matchingCapabilitieses;
        this.rigs = rigs;
        this.matchingCapabilitieses_1 = matchingCapabilitieses_1;
        this.rigs_1 = rigs_1;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId()
    {
        return this.id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    @Column(name = "capabilities", nullable = false, length = 65535)
    public String getCapabilities()
    {
        return this.capabilities;
    }

    public void setCapabilities(final String capabilities)
    {
        this.capabilities = capabilities;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigCapabilities")
    public Set<MatchingCapabilities> getMatchingCapabilitieses()
    {
        return this.matchingCapabilitieses;
    }

    public void setMatchingCapabilitieses(
            final Set<MatchingCapabilities> matchingCapabilitieses)
    {
        this.matchingCapabilitieses = matchingCapabilitieses;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigCapabilities")
    public Set<Rig> getRigs()
    {
        return this.rigs;
    }

    public void setRigs(final Set<Rig> rigs)
    {
        this.rigs = rigs;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigCapabilities")
    public Set<MatchingCapabilities> getMatchingCapabilitieses_1()
    {
        return this.matchingCapabilitieses_1;
    }

    public void setMatchingCapabilitieses_1(
            final Set<MatchingCapabilities> matchingCapabilitieses_1)
    {
        this.matchingCapabilitieses_1 = matchingCapabilitieses_1;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigCapabilities")
    public Set<Rig> getRigs_1()
    {
        return this.rigs_1;
    }

    public void setRigs_1(final Set<Rig> rigs_1)
    {
        this.rigs_1 = rigs_1;
    }

}
