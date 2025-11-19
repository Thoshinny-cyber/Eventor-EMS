import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { profileService } from '../services/profileService';
import { toast } from 'react-toastify';

const Profile = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    username: '',
    email: '',
    phone: '',
    gender: '',
    address: '',
    password: '',
    confirmPassword: '',
  });

  useEffect(() => {
    console.log('Current user from AuthContext:', user);
    if (user?.id) {
      loadProfile();
    } else {
      setLoading(false);
    }
  }, [user]);

const loadProfile = async () => {
  if (!user?.id) {
    setLoading(false);
    return;
  }

  try {
    const profileData = await profileService.getUserProfile(user.id);
    console.log('Profile data loaded:', profileData);

    if (!profileData || !profileData.id) {
      throw new Error('Profile data is empty');
    }

    setProfile(profileData);
    setFormData({
      name: profileData.name || '',
      username: profileData.username || '',
      email: profileData.email || '',
      phone: profileData.phone || '',
      gender: profileData.gender || '',
      address: profileData.address || '',
      password: '',
      confirmPassword: '',
    });
  } catch (error) {
    console.error('Error loading profile:', error);
    toast.error('Failed to load profile');
  } finally {
    setLoading(false);
  }
};

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleUpdateProfile = async (e) => {
    e.preventDefault();

    if (formData.password && formData.password !== formData.confirmPassword) {
      toast.error('Passwords do not match');
      return;
    }

    try {
      const updateData = { ...formData, id: user.id };
      if (!updateData.password) {
        delete updateData.password;
        delete updateData.confirmPassword;
      }

      await profileService.updateProfile(updateData);
      toast.success('Profile updated successfully');
      setEditing(false);
      loadProfile();
    } catch (error) {
      console.error('Error updating profile:', error);
      toast.error('Failed to update profile');
    }
  };

  if (loading) {
    return <div className="loading">Loading profile...</div>;
  }

  if (!profile) {
    return <div className="error">Profile not found</div>;
  }

  return (
    <div className="profile-container">
      <h1>My Profile</h1>

      <div className="profile-content">
        <div className="profile-form-section">
          {!editing ? (
            <div className="profile-info">
              <p><strong>Name:</strong> {profile.name}</p>
              <p><strong>Username:</strong> {profile.username}</p>
              <p><strong>Email:</strong> {profile.email}</p>
              <p><strong>Phone:</strong> {profile.phone}</p>
              <p><strong>Gender:</strong> {profile.gender}</p>
              <p><strong>Address:</strong> {profile.address || 'N/A'}</p>
              <button className="btn-primary" onClick={() => setEditing(true)}>
                Edit Profile
              </button>
            </div>
          ) : (
            <form onSubmit={handleUpdateProfile} className="profile-form">
              {['name','username','email','phone','gender','address','password','confirmPassword'].map((field) => (
                <div key={field}>
                  <label>{field.charAt(0).toUpperCase() + field.slice(1)}:</label>
                  <input
                    type={field.includes('password') ? 'password' : 'text'}
                    name={field}
                    value={formData[field]}
                    onChange={handleChange}
                  />
                </div>
              ))}

              <div className="form-actions">
                <button type="submit" className="btn-primary">Save Changes</button>
                <button
                  type="button"
                  className="btn-secondary"
                  onClick={() => {
                    setEditing(false);
                    loadProfile();
                  }}
                >
                  Cancel
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

export default Profile;
