package systemB;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

import static systemB.Constants.HEADER_LENGTH;

public class SourceFilter extends FilterFramework
{
	private final String fileName;

	public SourceFilter(String fileName) {
		this.fileName = fileName;
	}

	public void run()
    {
		int bytes_read = 0;
		int bytes_written = 0;
		DataInputStream in = null;

		try
		{

			in = new DataInputStream(new FileInputStream(fileName));
			System.out.println("\n" + this.getName() + "::Source reading file..." );


			while(true)
			{
				byte[] data = new byte[HEADER_LENGTH];
				in.readFully(data);
				bytes_read+=HEADER_LENGTH;
				WriteFilterOutputPort(data);
				bytes_written+=HEADER_LENGTH;
			}

		}
		catch ( EOFException eoferr )
		{
			System.out.println("\n" + this.getName() + "::End of file reached..." );
			try
			{
				in.close();
				ClosePorts();
				System.out.println( "\n" + this.getName() + "::Read file complete, bytes read::" + bytes_read + " bytes written: " + bytes_written );

			}
			catch (Exception closeerr)
			{
				System.out.println("\n" + this.getName() + "::Problem closing input data file::" + closeerr);

			}
		}
		catch ( IOException iox )
		{
			System.out.println("\n" + this.getName() + "::Problem reading input data file::" + iox );

		}
   }
}